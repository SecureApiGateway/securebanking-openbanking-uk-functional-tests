package com.forgerock.sapi.gateway.ob.uk.tests.functional.account.standing.orders.junit.v4_0_0

import com.forgerock.sapi.gateway.framework.extensions.junit.CreateTppCallback
import com.forgerock.sapi.gateway.framework.extensions.junit.EnabledIfVersion
import com.forgerock.sapi.gateway.uk.common.shared.api.meta.obie.OBVersion
import com.forgerock.sapi.gateway.ob.uk.tests.functional.account.standing.orders.api.v4_0_0.GetStandingOrders
import org.junit.jupiter.api.Test

class GetStandingOrdersTest(val tppResource: CreateTppCallback.TppResource) {
    @EnabledIfVersion(
        type = "accounts",
        apiVersion = "v4.0.0",
        operations = ["CreateAccountAccessConsent", "GetAccounts", "GetStandingOrders"],
        apis = ["standing-orders"]
    )
    @Test
    fun shouldGetStandingOrders_v4_0_0() {
        GetStandingOrders(OBVersion.v4_0_0, tppResource).shouldGetStandingOrdersTest()
    }
}