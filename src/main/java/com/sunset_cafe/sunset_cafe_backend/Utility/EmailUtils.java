package com.sunset_cafe.sunset_cafe_backend.Utility;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailUtils {

    private final JavaMailSender javaMailSender;

    public void sendSimpleMessage(String to, String subject, String text, List<String> list){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);

        if (list != null && list.size() > 0){
            simpleMailMessage.setCc(getCcArray(list));
        }
        javaMailSender.send(simpleMailMessage);
    }

    private String[] getCcArray(List<String> ccList){
        String[] cc = new String[ccList.size()];
        for (int i = 0; i < ccList.size() ; i++){
            cc[i] = ccList.get(i);
        }
        return cc;
    }

}
