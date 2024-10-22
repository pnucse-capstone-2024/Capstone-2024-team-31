package com.wasin.wasin.domain.dto;

import java.util.List;

public class CompanyResponse {

    public record DBList(
            List<CompanyDBItem> companyDBList
    ){
        public record CompanyDBItem (
                Long companyId,
                String location,
                String companyName
        ) {
        }
    }

    public record OpenAPIList(
            List<CompanyOpenAPIItem> companyOpenAPIList
    ){
        public record CompanyOpenAPIItem (
                String companyFssId,
                String location,
                String companyName
        ) {
        }
    }

}
