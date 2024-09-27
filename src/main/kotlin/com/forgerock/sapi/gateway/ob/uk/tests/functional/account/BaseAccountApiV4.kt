package com.forgerock.sapi.gateway.ob.uk.tests.functional.account

import com.forgerock.sapi.gateway.framework.extensions.junit.CreateTppCallback
import com.forgerock.sapi.gateway.ob.uk.support.discovery.getAccountsApiLinks
import com.forgerock.sapi.gateway.uk.common.shared.api.meta.obie.OBVersion
import com.forgerock.sapi.gateway.ob.uk.tests.functional.account.access.consents.AccountAccessConsentApiV4

/**
 * Base class for Accounts Api classes to extend from, provides an AccountAccessConsentApi instance which can be
 * used to obtain the consents required to carry out Api operations.
 */
open class BaseAccountApiV4(
    val version: OBVersion,
    val accountAccessConsentApi: AccountAccessConsentApiV4,
    val tppResource: CreateTppCallback.TppResource
) {
    protected val accountsApiLinks = getAccountsApiLinks(version)
}
