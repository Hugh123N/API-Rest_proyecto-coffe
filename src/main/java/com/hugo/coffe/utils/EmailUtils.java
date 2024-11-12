package com.hugo.coffe.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender emailSender;

    public void sentSimpleMessage(String to, String subject, String text, List<String> list){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("hughhnp@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        if(list!=null && list.size() > 0)
            message.setCc(getCcArray(list));
        emailSender.send(message);
    }

    private String[] getCcArray(List<String> ccList){
        String[] cc=new String[ccList.size()];
        for(int i=0; i< ccList.size(); i++){
            cc[i] =ccList.get(i);
        }
        return cc;
    }

    //mensaje de correo cuando olvidas el password
    public void olvidoEmail(String to,String subject, String password) throws MessagingException {
        MimeMessage message=emailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);
        helper.setFrom("hughhnp@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        String htmlMsg="<p>" +
                "<b>Sus datos de acceso para la gestion del cafe</b><br>" +
                "<b> Email: </b> "+to+"<br>" +
                "<b>Password: </b> "+password+"<br>" +
                "<a href=\"http://localhost:4200/\"> Haga clic aqui para iniciar sesion</a>" +
                "</p>";
        message.setContent(htmlMsg,"text/html");
        emailSender.send(message);
    }
}
