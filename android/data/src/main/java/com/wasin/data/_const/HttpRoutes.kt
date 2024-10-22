package com.wasin.data._const


enum class HttpRoutes(
    val path: String
) {
    BASE_HOST("api.wasin.site"),
    BASE_V1_PATH("api/"),

    // user
    SIGN_UP("user/signup"),
    LOGIN("user/login"),
    LOGOUT("user/logout"),
    WITHDRAW("user/withdraw"),
    LOCK("user/lock"),
    LOCK_CONFIRM("user/lock/confirm"),
    EMAIL_CODE_CONFIRM("user/auth/email/check"),
    EMAIL_CODE_SEND("user/auth/email"),
    REISSUE("user/auth/reissue"),

    // company
    GET_OPEN_API("company/open-api/list"),
    GET_DB("company/db"),
    SAVE_COMPANY_OPEN_API("company/open-api"),
    SAVE_COMPANY_DB("company/db"),

    // backoffice
    BACKOFFICE_ACCEPT("backoffice/accept"),
    GET_WAITING_LIST("backoffice"),

    // router
    ROUTER("router"),
    GET_COMPANY_IMAGE("router/image"),
    DELETE_ROUTER("router/delete"),
    ROUTER_CHECK("router/check"),
    ROUTER_LOG("router/log"),
    ROUTER_EMAIL_LOG("router/log/email"),

    // profile
    GET_PROFILE("profile"),
    PROFILE_MODE_AUTO_CHANGE("profile/mode/auto"),
    PROFILE_MODE_MANUAL_CHANGE("profile/mode/manual"),
    UPDATE_PROFILE("profile"),

    // hand off
    GET_HAND_OFF_ALL("hand-off"),
    GET_HAND_OFF_BEST("hand-off/best"),
    CHANGE_MODE_AUTO("hand-off/mode/auto"),
    CHANGE_MODE_MANUAL("hand-off/mode/manual"),

    // monitoring
    MONITORING_BY_ID("monitoring"),
    MONITORING_MULTIPLE("monitoring"),

    ;

    fun getPath1(): String {
        return "v1/" + this.path
    }
}
