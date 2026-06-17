package com.stu.gym.gymmembermanage.controller;

import com.stu.gym.gymmembermanage.entity.Member;
import com.stu.gym.gymmembermanage.entity.Course;
import com.stu.gym.gymmembermanage.entity.Reservation;
import com.stu.gym.gymmembermanage.service.MemberService;
import com.stu.gym.gymmembermanage.service.CourseService;
import com.stu.gym.gymmembermanage.service.ReservationService;
import com.stu.gym.gymmembermanage.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 管理员控制器
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ReservationService reservationService;

    /**
     * 管理后台首页
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("memberCount", memberService.countAll());
        model.addAttribute("activeMemberCount", memberService.countActive());
        model.addAttribute("courseCount", courseService.countAll());
        model.addAttribute("activeCourseCount", courseService.countActive());
        model.addAttribute("reservationCount", reservationService.countAll());
        model.addAttribute("activeReservationCount", reservationService.countActive());
        return "admin/dashboard";
    }

    // ==================== 会员管理 ====================

    /**
     * 会员列表页面（分页）
     */
    @GetMapping("/member/list")
    public String memberList(@RequestParam(required = false) String realName,
                             @RequestParam(required = false) String phone,
                             @RequestParam(required = false) Integer status,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             Model model) {
        PageResult<Member> pageResult;
        if (realName != null || phone != null || status != null) {
            pageResult = memberService.searchMembersByPage(realName, phone, status, pageNum, 10);
        } else {
            pageResult = memberService.getMembersByPage(pageNum, 10);
        }
        model.addAttribute("pageResult", pageResult);
        model.addAttribute("realName", realName);
        model.addAttribute("phone", phone);
        model.addAttribute("status", status);
        return "admin/member/list";
    }

    /**
     * 会员添加页面
     */
    @GetMapping("/member/add")
    public String memberAddPage() {
        return "admin/member/form";
    }

    /**
     * 会员编辑页面
     */
    @GetMapping("/member/edit/{id}")
    public String memberEditPage(@PathVariable Integer id, Model model) {
        Member member = memberService.getMemberById(id);
        if (member == null) {
            return "redirect:/admin/member/list";
        }
        model.addAttribute("member", member);
        return "admin/member/form";
    }

    /**
     * 保存会员(添加或更新)
     */
    @PostMapping("/member/save")
    public String memberSave(Member member, RedirectAttributes redirectAttributes) {
        if (member.getId() == null) {
            boolean result = memberService.addMember(member);
            if (result) {
                redirectAttributes.addFlashAttribute("message", "会员添加成功");
            } else {
                redirectAttributes.addFlashAttribute("error", "会员添加失败，用户名可能已存在");
            }
        } else {
            boolean result = memberService.updateMember(member);
            if (result) {
                redirectAttributes.addFlashAttribute("message", "会员更新成功");
            } else {
                redirectAttributes.addFlashAttribute("error", "会员更新失败");
            }
        }
        return "redirect:/admin/member/list";
    }

    /**
     * 删除会员
     */
    @GetMapping("/member/delete/{id}")
    public String memberDelete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        boolean result = memberService.deleteMember(id);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "会员删除成功");
        } else {
            redirectAttributes.addFlashAttribute("error", "会员删除失败");
        }
        return "redirect:/admin/member/list";
    }

    /**
     * 更新会员状态
     */
    @GetMapping("/member/status/{id}/{status}")
    public String memberStatus(@PathVariable Integer id, @PathVariable Integer status, RedirectAttributes redirectAttributes) {
        boolean result = memberService.updateMemberStatus(id, status);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "状态更新成功");
        } else {
            redirectAttributes.addFlashAttribute("error", "状态更新失败");
        }
        return "redirect:/admin/member/list";
    }

    // ==================== 课程管理 ====================

    /**
     * 课程列表页面（分页）
     */
    @GetMapping("/course/list")
    public String courseList(@RequestParam(required = false) String name,
                             @RequestParam(required = false) String coach,
                             @RequestParam(required = false) Integer status,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             Model model) {
        PageResult<Course> pageResult;
        if (name != null || coach != null || status != null) {
            pageResult = courseService.searchCoursesByPage(name, coach, status, pageNum, 10);
        } else {
            pageResult = courseService.getCoursesByPage(pageNum, 10);
        }
        model.addAttribute("pageResult", pageResult);
        model.addAttribute("name", name);
        model.addAttribute("coach", coach);
        model.addAttribute("status", status);
        return "admin/course/list";
    }

    /**
     * 课程添加页面
     */
    @GetMapping("/course/add")
    public String courseAddPage() {
        return "admin/course/form";
    }

    /**
     * 课程编辑页面
     */
    @GetMapping("/course/edit/{id}")
    public String courseEditPage(@PathVariable Integer id, Model model) {
        Course course = courseService.getCourseById(id);
        if (course == null) {
            return "redirect:/admin/course/list";
        }
        model.addAttribute("course", course);
        return "admin/course/form";
    }

    /**
     * 保存课程(添加或更新)
     */
    @PostMapping("/course/save")
    public String courseSave(Course course, RedirectAttributes redirectAttributes) {
        if (course.getId() == null) {
            boolean result = courseService.addCourse(course);
            if (result) {
                redirectAttributes.addFlashAttribute("message", "课程添加成功");
            } else {
                redirectAttributes.addFlashAttribute("error", "课程添加失败");
            }
        } else {
            boolean result = courseService.updateCourse(course);
            if (result) {
                redirectAttributes.addFlashAttribute("message", "课程更新成功");
            } else {
                redirectAttributes.addFlashAttribute("error", "课程更新失败");
            }
        }
        return "redirect:/admin/course/list";
    }

    /**
     * 删除课程
     */
    @GetMapping("/course/delete/{id}")
    public String courseDelete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        boolean result = courseService.deleteCourse(id);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "课程删除成功");
        } else {
            redirectAttributes.addFlashAttribute("error", "课程删除失败");
        }
        return "redirect:/admin/course/list";
    }

    /**
     * 更新课程状态
     */
    @GetMapping("/course/status/{id}/{status}")
    public String courseStatus(@PathVariable Integer id, @PathVariable Integer status, RedirectAttributes redirectAttributes) {
        boolean result = courseService.updateCourseStatus(id, status);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "状态更新成功");
        } else {
            redirectAttributes.addFlashAttribute("error", "状态更新失败");
        }
        return "redirect:/admin/course/list";
    }

    // ==================== 预约管理 ====================

    /**
     * 预约记录列表页面（分页）
     */
    @GetMapping("/reservation/list")
    public String reservationList(@RequestParam(required = false) Integer memberId,
                                  @RequestParam(required = false) Integer courseId,
                                  @RequestParam(required = false) Integer status,
                                  @RequestParam(defaultValue = "1") Integer pageNum,
                                  Model model) {
        PageResult<Reservation> pageResult;
        if (memberId != null || courseId != null || status != null) {
            pageResult = reservationService.searchReservationsByPage(memberId, courseId, status, pageNum, 10);
        } else {
            pageResult = reservationService.getReservationsByPage(pageNum, 10);
        }
        model.addAttribute("pageResult", pageResult);
        model.addAttribute("memberId", memberId);
        model.addAttribute("courseId", courseId);
        model.addAttribute("status", status);
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("courses", courseService.getAllCourses());
        return "admin/reservation/list";
    }

    /**
     * 取消预约
     */
    @GetMapping("/reservation/cancel/{id}")
    public String reservationCancel(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        boolean result = reservationService.adminCancelReservation(id);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "预约已取消");
        } else {
            redirectAttributes.addFlashAttribute("error", "取消失败");
        }
        return "redirect:/admin/reservation/list";
    }

    /**
     * 删除预约记录
     */
    @GetMapping("/reservation/delete/{id}")
    public String reservationDelete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        boolean result = reservationService.deleteReservation(id);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "记录删除成功");
        } else {
            redirectAttributes.addFlashAttribute("error", "记录删除失败");
        }
        return "redirect:/admin/reservation/list";
    }
}
