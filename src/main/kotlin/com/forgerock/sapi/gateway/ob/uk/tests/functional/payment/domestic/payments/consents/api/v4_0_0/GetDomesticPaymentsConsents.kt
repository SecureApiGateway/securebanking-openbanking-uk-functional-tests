package com.forgerock.sapi.gateway.ob.uk.tests.functional.payment.domestic.payments.consents.api.v4_0_0

import assertk.assertThat
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.forgerock.sapi.gateway.framework.conditions.StatusV4
import com.forgerock.sapi.gateway.framework.extensions.junit.CreateTppCallback
import com.forgerock.sapi.gateway.ob.uk.framework.consent.ConsentFactoryRegistryHolder
import com.forgerock.sapi.gateway.ob.uk.framework.consent.payment.v4.OBWriteDomesticConsent4Factory
import com.forgerock.sapi.gateway.ob.uk.support.discovery.getPaymentsApiLinks
import com.forgerock.sapi.gateway.uk.common.shared.api.meta.obie.OBVersion
import org.assertj.core.api.Assertions

class GetDomesticPaymentsConsents(val version: OBVersion, val tppResource: CreateTppCallback.TppResource) {

    private val createDomesticPaymentsConsentsApi = CreateDomesticPaymentsConsents(version, tppResource)
    private val paymentLinks = getPaymentsApiLinks(version)
    private val consentFactory: OBWriteDomesticConsent4Factory = ConsentFactoryRegistryHolder.consentFactoryRegistry.getConsentFactory(
        OBWriteDomesticConsent4Factory::class.java)

    fun shouldGetDomesticPaymentsConsents() {
        // Given
        val consentRequest = consentFactory.createConsent()
        // When
        val consentResponse = createDomesticPaymentsConsentsApi.createDomesticPaymentsConsent(consentRequest)
        // Then
        assertThat(consentResponse).isNotNull()
        assertThat(consentResponse.data).isNotNull()
        assertThat(consentResponse.data.consentId).isNotEmpty()
        Assertions.assertThat(consentResponse.data.status.toString()).`is`(StatusV4.consentCondition)
        assertThat(consentResponse.risk).isNotNull()
    }

    fun shouldGetDomesticPaymentsConsents_withoutOptionalDebtorAccountTest() {
        // Given
        val consentRequest = consentFactory.createConsent()
        consentRequest.data.initiation.debtorAccount(null)
        // when
        val consentResponse = createDomesticPaymentsConsentsApi.createDomesticPaymentsConsent(consentRequest)
        // Then
        assertThat(consentResponse).isNotNull()
        assertThat(consentResponse.data).isNotNull()
        assertThat(consentResponse.data.consentId).isNotEmpty()
        Assertions.assertThat(consentResponse.data.status.toString()).`is`(StatusV4.consentCondition)
        assertThat(consentResponse.risk).isNotNull()
        assertThat(consentResponse.data.initiation.debtorAccount).isNull()
    }
}