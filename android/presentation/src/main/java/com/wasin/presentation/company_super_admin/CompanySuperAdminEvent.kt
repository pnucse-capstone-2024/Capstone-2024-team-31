package com.wasin.presentation.company_super_admin

import java.io.File

sealed class CompanySuperAdminEvent {
    data class EnterCode(val code: String): CompanySuperAdminEvent()
    data class EnterCompany(val name: String): CompanySuperAdminEvent()
    data class EnterImage(val file: File): CompanySuperAdminEvent()
    data class SelectCompany(val name: String, val companyFssId: String, val location: String): CompanySuperAdminEvent()
    data object SearchCompany: CompanySuperAdminEvent()
    data object Save: CompanySuperAdminEvent()
}
