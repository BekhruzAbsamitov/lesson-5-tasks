package uz.pdp.demo.dto;

import lombok.Data;

import java.sql.Date;


@Data
public class SalaryHistoryDto {

    private Integer userId;
    private Integer amount;
    private Date workEndDate;
    private Date workStartDate;
}
