package com.stu.gym.gymmembermanage.controller;

import com.stu.gym.gymmembermanage.entity.Member;
import com.stu.gym.gymmembermanage.entity.Reservation;
import com.stu.gym.gymmembermanage.entity.Course;
import com.stu.gym.gymmembermanage.service.AdminService;
import com.stu.gym.gymmembermanage.service.MemberService;
import com.stu.gym.gymmembermanage.service.CourseService;
import com.stu.gym.gymmembermanage.service.ReservationService;
import com.stu.gym.gymmembermanage.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * 会员端控制器
 */
@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private AdminService adminService;

    /**
     * 首页
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 登录页面
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * 注册页面
     */
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    /**
     * 执行登录
     */
    @PostMapping("/doLogin")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String role,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        if ("admin".equals(role)) {
            com.stu.gym.gymmembermanage.entity.Admin admin = adminService.login(username, password);
            if (admin != null) {
                session.setAttribute("adminUser", admin);
                session.setAttribute("loginRole", "admin");
                return "redirect:/admin/dashboard";
            }
        } else {
            Member member = memberService.login(username, password);
            if (member != null) {
                session.setAttribute("memberUser", member);
                session.setAttribute("loginRole", "member");
                return "redirect:/member/index";
            }
        }
        redirectAttributes.addFlashAttribute("error", "用户名或密码错误");
        return "redirect:/login";
    }

    /**
     * 执行注册
     */
    @PostMapping("/doRegister")
    public String doRegister(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String realName,
                             @RequestParam(required = false) Integer gender,
                             @RequestParam(required = false) String phone,
                             @RequestParam(required = false) String email,
                             RedirectAttributes redirectAttributes) {
        Member member = new Member();
        member.setUsername(username);
        member.setPassword(password);
        member.setRealName(realName);
        member.setGender(gender);
        member.setPhone(phone);
        member.setEmail(email);

        boolean result = memberService.register(member);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "注册成功，请登录");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "注册失败，用户名可能已存在");
            return "redirect:/register";
        }
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ==================== 会员端功能 ====================

    /**
     * 会员首页
     */
    @GetMapping("/member/index")
    public String memberIndex(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("memberUser");
        if (member == null) {
            return "redirect:/login";
        }
        PageResult<Reservation> pageResult = reservationService.getReservationsByMemberIdPage(member.getId(), 1, 100);
        long activeReservations = pageResult.getList().stream().filter(r -> r.getStatus() == 1).count();
        model.addAttribute("member", member);
        model.addAttribute("reservationCount", activeReservations);
        model.addAttribute("courseCount", courseService.countActive());
        return "member/index";
    }

    /**
     * 课程列表页面（分页）
     */
    @GetMapping("/member/course/list")
    public String memberCourseList(@RequestParam(defaultValue = "1") Integer pageNum,
                                   HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("memberUser");
        if (member == null) {
            return "redirect:/login";
        }
        PageResult<Course> pageResult = courseService.getActiveCoursesByPage(pageNum, 10);
        model.addAttribute("pageResult", pageResult);
        model.addAttribute("member", member);
        return "member/course/list";
    }

    /**
     * 预约课程
     */
    @GetMapping("/member/reservation/make/{courseId}")
    public String makeReservation(@PathVariable Integer courseId,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        Member member = (Member) session.getAttribute("memberUser");
        if (member == null) {
            return "redirect:/login";
        }
        String result = reservationService.makeReservation(member.getId(), courseId);
        if ("预约成功".equals(result)) {
            redirectAttributes.addFlashAttribute("message", result);
        } else {
            redirectAttributes.addFlashAttribute("error", result);
        }
        return "redirect:/member/course/list";
    }

    /**
     * 我的预约页面（分页）
     */
    @GetMapping("/member/reservation/list")
    public String memberReservationList(@RequestParam(defaultValue = "1") Integer pageNum,
                                        HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("memberUser");
        if (member == null) {
            return "redirect:/login";
        }
        PageResult<Reservation> pageResult = reservationService.getReservationsByMemberIdPage(member.getId(), pageNum, 10);
        model.addAttribute("pageResult", pageResult);
        model.addAttribute("member", member);
        return "member/reservation/list";
    }

    /**
     * 取消预约
     */
    @GetMapping("/member/reservation/cancel/{reservationId}")
    public String cancelReservation(@PathVariable Integer reservationId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        Member member = (Member) session.getAttribute("memberUser");
        if (member == null) {
            return "redirect:/login";
        }
        String result = reservationService.cancelReservation(reservationId, member.getId());
        if ("取消成功".equals(result)) {
            redirectAttributes.addFlashAttribute("message", result);
        } else {
            redirectAttributes.addFlashAttribute("error", result);
        }
        return "redirect:/member/reservation/list";
    }

    /**
     * 个人中心页面
     */
    @GetMapping("/member/profile")
    public String memberProfile(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("memberUser");
        if (member == null) {
            return "redirect:/login";
        }
        Member latestMember = memberService.getMemberById(member.getId());
        model.addAttribute("member", latestMember);
        return "member/profile";
    }

    /**
     * 智能客服页面
     */
    @GetMapping("/member/ai/chat")
    public String aiChatPage(HttpSession session) {
        Member member = (Member) session.getAttribute("memberUser");
        if (member == null) {
            return "redirect:/login";
        }
        return "member/ai/chat";
    }

    /**
     * 智能推荐页面
     */
    @GetMapping("/member/ai/recommend")
    public String aiRecommendPage(HttpSession session) {
        Member member = (Member) session.getAttribute("memberUser");
        if (member == null) {
            return "redirect:/login";
        }
        return "member/ai/recommend";
    }

    /**
     * 更新个人信息
     */
    @PostMapping("/member/profile/update")
    public String updateProfile(@RequestParam String realName,
                                @RequestParam(required = false) Integer gender,
                                @RequestParam(required = false) String phone,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String password,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Member member = (Member) session.getAttribute("memberUser");
        if (member == null) {
            return "redirect:/login";
        }
        Member updateMember = new Member();
        updateMember.setId(member.getId());
        updateMember.setRealName(realName);
        updateMember.setGender(gender);
        updateMember.setPhone(phone);
        updateMember.setEmail(email);
        if (password != null && !password.isEmpty()) {
            updateMember.setPassword(password);
        }

        boolean result = memberService.updateMember(updateMember);
        if (result) {
            Member latestMember = memberService.getMemberById(member.getId());
            session.setAttribute("memberUser", latestMember);
            redirectAttributes.addFlashAttribute("message", "个人信息更新成功");
        } else {
            redirectAttributes.addFlashAttribute("error", "更新失败");
        }
        return "redirect:/member/profile";
    }
}
