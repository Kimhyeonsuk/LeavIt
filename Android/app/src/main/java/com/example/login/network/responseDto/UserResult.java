package com.example.login.network.responseDto;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.example.login.network.enumclass.UserStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class UserResult {
    private Long id;

    private String account;

    private String password;

    private UserStatus status;

    private String email;

    private String phoneNumber;

    private LocalDateTime registeredAt;

    private LocalDateTime unregisteredAt;

    private List<PostResult> postResultList;

    @Override
    public String toString(){
        DateTimeFormatter formatter=DateTimeFormatter.BASIC_ISO_DATE;
        return "UserResult{"+
                "id="+id+
                ", account="+account+
                ", password="+password+
                ", status="+status.toString()+
                ", email="+email+
                ", phoneNumber="+phoneNumber+
                ", registeredAt="+registeredAt.format(formatter)+
                ", unregisterdAt="+unregisteredAt.format(formatter);
    }
}
