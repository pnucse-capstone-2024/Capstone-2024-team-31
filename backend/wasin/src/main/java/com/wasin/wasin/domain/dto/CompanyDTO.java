package com.wasin.wasin.domain.dto;

import java.util.List;

public class CompanyDTO {

    public record ResponseValue(
       Response response
    ) {}

    public record Response(
            Body body,
            Header header
    ) {}

    public record Body(
            Items items,
            int numOfRows,
            int pageNo,
            int totalCount
    ) {}

    public record Items(
            List<Item> item
    ) {}

    public record Item(
            String crno,
            String corpNm,
            String corpEnsnNm,
            String enpPbanCmpyNm,
            String enpRprFnm,
            String corpRegMrktDcd,
            String corpRegMrktDcdNm,
            String corpDcd,
            String corpDcdNm,
            String bzno,
            String enpOzpno,
            String enpBsadr,
            String enpDtadr,
            String enpHmpgUrl,
            String enpTlno,
            String enpFxno,
            String sicNm,
            String enpEstbDt,
            String enpStacMm,
            String enpXchgLstgDt,
            String enpXchgLstgAbolDt,
            String enpKosdaqLstgDt,
            String enpKosdaqLstgAbolDt,
            String enpKrxLstgDt,
            String enpKrxLstgAbolDt,
            String smenpYn,
            String enpMntrBnkNm,
            String enpEmpeCnt,
            String empeAvgCnwkTermCtt,
            String enpPn1AvgSlryAmt,
            String actnAudpnNm,
            String audtRptOpnnCtt,
            String enpMainBizNm,
            String fssCorpUnqNo,
            String fssCorpChgDtm,
            String fstOpegDt,
            String lastOpegDt
    ) {}

    public record Header(
            String resultCode,
            String resultMsg
    ) {}

}


