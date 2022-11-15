package com.Capstone.Lincall.controller;

import com.Capstone.Lincall.domain.User;
import com.Capstone.Lincall.service.EmailService;
import com.Capstone.Lincall.service.RoomService;
import com.Capstone.Lincall.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
@RequestMapping(value="/user")
@CrossOrigin(origins = "*")
public class UserController {

    UserService userService;
    RoomService roomService;
    EmailService emailService;

    @Getter
    static class LogInModel{
        private String id;
        private String password;
    }

    @Autowired
    UserController(UserService userService, RoomService roomService, EmailService emailService){

        this.userService = userService;
        this.roomService = roomService;
        this.emailService = emailService;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                             //
    //                                      /user/client                                           //
    //                                                                                             //
    /////////////////////////////////////////////////////////////////////////////////////////////////

    @PostMapping("/client/signUp")
    @ResponseBody
    public boolean clientSignUp(@RequestBody User user){
        return userService.clientSignUp(user);
    }

    @PostMapping("/client/logIn")
    @ResponseBody
    public User clientLogIn(@RequestBody LogInModel model){
        return userService.clientLogIn(model.getId(), model.getPassword());
    }

    @GetMapping("/client/id-check")
    @ResponseBody
    public boolean clientIdCheck(String id){
        return userService.clientIdAvailable(id);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                             //
    //                                      /user/counselor                                        //
    //                                                                                             //
    /////////////////////////////////////////////////////////////////////////////////////////////////

    @PostMapping("/counselor/signUp")
    @ResponseBody
    public boolean counselorSignUp(@RequestBody User user){
        return userService.counselorSignUp(user);
    }

    @PostMapping("/counselor/logIn")
    @ResponseBody
    public User counselorLogIn(@RequestBody LogInModel model){
        return userService.counselorLogIn(model.getId(), model.getPassword());
    }

    @GetMapping("/counselor/id-check")
    @ResponseBody
    public boolean counselorIdCheck(String id){
        return userService.counselorIdAvailable(id);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                             //
    //                                            /user                                            //
    //                                                                                             //
    /////////////////////////////////////////////////////////////////////////////////////////////////

    // webRTC room 생성 test code
    @PostMapping("/rooms-test")
    @ResponseBody
    public List<String> clientSignUp(){
        return roomService.getAvailableRooms();
    }

    // 메일로 인증 키 전송
    @GetMapping("/email-auth")
    @ResponseBody
    public String mailCheck(String email) throws ExecutionException, InterruptedException {
        // 메일 전송은 비동기 처리
        Future<String> future = emailService.sendEmail(email);
        return future.get();    // 인증 키 반환
    }

    // 프로필 사진 저장
    @PostMapping("/profile")
    @ResponseBody
    public void saveProfileImage(
            @RequestPart("id") String userID,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        String path = new File("../").getAbsolutePath();
        String filename = path + File.separator + "image"+ File.separator + "profile" + File.separator + userID + ".png";

        Path p = Paths.get(filename);
        image.transferTo(p);

    }

}
