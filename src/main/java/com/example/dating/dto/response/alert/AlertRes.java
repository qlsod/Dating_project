package com.example.dating.dto.response.alert;

import com.example.dating.domain.Alert;
import com.example.dating.dto.alert.AlertDto;
import lombok.Data;

import java.util.List;

@Data
public class AlertRes {

    List<AlertDto> imageList;

}
