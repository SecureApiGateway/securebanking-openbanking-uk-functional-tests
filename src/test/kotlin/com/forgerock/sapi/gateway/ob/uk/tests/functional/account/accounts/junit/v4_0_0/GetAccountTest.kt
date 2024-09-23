package com.forgerock.sapi.gateway.ob.uk.tests.functional.account.accounts.junit.v4_0_0

import com.forgerock.sapi.gateway.framework.extensions.junit.CreateTppCallback
import com.forgerock.sapi.gateway.framework.extensions.junit.EnabledIfVersion
import com.forgerock.sapi.gateway.uk.common.shared.api.meta.obie.OBVersion
import com.forgerock.sapi.gateway.ob.uk.tests.functional.account.accounts.api.v3_1_8.GetAccount
import org.junit.jupiter.api.Test


class GetAccountTest(val tppResource: CreateTppCallback.TppResource) {

    @EnabledIfVersion(
        type = "accounts",
        apiVersion = "v3.1.10",
        operations = ["CreateAccountAccessConsent", "GetAccounts", "GetAccount"],
        apis = ["accounts"]
    )
    @Test
    fun shouldGetAccount_v3_1_10() {
        GetAccount(OBVersion.v3_1_10, tppResource).shouldGetAccountTest()
    }
}