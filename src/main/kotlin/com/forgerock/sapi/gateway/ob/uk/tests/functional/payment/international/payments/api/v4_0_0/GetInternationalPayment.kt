package com.forgerock.sapi.gateway.ob.uk.tests.functional.payment.international.payments.api.v4_0_0

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import com.forgerock.sapi.gateway.framework.conditions.StatusV4
import com.forgerock.sapi.gateway.framework.extensions.junit.CreateTppCallback
import com.forgerock.sapi.gateway.ob.uk.framework.consent.ConsentFactoryRegistryHolder
import com.forgerock.sapi.gateway.ob.uk.framework.consent.payment.v4.OBWriteInternationalConsent5Factory
import com.forgerock.sapi.gateway.ob.uk.support.discovery.getPaymentsApiLinks
import com.forgerock.sapi.gateway.ob.uk.support.payment.v4.PaymentFactory
import com.forgerock.sapi.gateway.ob.uk.support.payment.defaultPaymentScopesForAccessToken
import com.forgerock.sapi.gateway.uk.common.shared.api.meta.obie.OBVersion
import org.assertj.core.api.Assertions
import uk.org.openbanking.datamodel.v4.payment.OBExchangeRateType
import uk.org.openbanking.datamodel.v4.common.OBReadRefundAccount
import uk.org.openbanking.datamodel.v4.payment.OBWriteInternationalResponse5

class GetInternationalPayment(
    val version: OBVersion,
    val tppResource: CreateTppCallback.TppResource
) {
    private val createInternationalPayment = CreateInternationalPayment(version, tppResource)
    private val paymentLinks = getPaymentsApiLinks(version)
    private val paymentApiClient = tppResource.tpp.paymentApiClient
    private val consentFactory = ConsentFactoryRegistryHolder.consentFactoryRegistry.getConsentFactory(
        OBWriteInternationalConsent5Factory::class.java
    )

    fun getInternationalPayments_rateType_AGREED_Test() {
        // Given
        val consentRequest = consentFactory.createConsent()
        consentRequest.data.initiation.exchangeRateInformation.rateType = OBExchangeRateType.AGREED
        val paymentResponse = createInternationalPayment.submitPayment(consentRequest)

        // When
        val result = getInternationalPayment(paymentResponse)

        // Then
        assertThat(result).isNotNull()
        assertThat(result.data.internationalPaymentId).isNotEmpty()
        assertThat(result.data.creationDateTime).isNotNull()
        assertThat(result.data.charges).isNotNull().isEmpty()
        Assertions.assertThat(result.data.status.toString()).`is`(StatusV4.paymentCondition)
        assertThat(result.data.exchangeRateInformation.exchangeRate).isNotNull()
    }

    fun getInternationalPayments_rateType_ACTUAL_Test() {
        // Given
        val consentRequest = consentFactory.createConsent()
        consentRequest.data.initiation.exchangeRateInformation.rateType = OBExchangeRateType.ACTUAL
        consentRequest.data.initiation.exchangeRateInformation.exchangeRate = null
        consentRequest.data.initiation.exchangeRateInformation.contractIdentification = null

        val paymentResponse = createInternationalPayment.submitPayment(consentRequest)

        // When
        val result = getInternationalPayment(paymentResponse)

        // Then
        assertThat(result).isNotNull()
        assertThat(result.data.internationalPaymentId).isNotEmpty()
        assertThat(result.data.creationDateTime).isNotNull()
        Assertions.assertThat(result.data.status.toString()).`is`(StatusV4.paymentCondition)
    }

    fun getInternationalPayments_rateType_INDICATIVE_Test() {
        // Given
        val consentRequest = consentFactory.createConsent()
        consentRequest.data.initiation.exchangeRateInformation.rateType = OBExchangeRateType.INDICATIVE
        consentRequest.data.initiation.exchangeRateInformation.exchangeRate = null
        consentRequest.data.initiation.exchangeRateInformation.contractIdentification = null

        val paymentResponse = createInternationalPayment.submitPayment(consentRequest)

        // When
        val result = getInternationalPayment(paymentResponse)

        // Then
        assertThat(result).isNotNull()
        assertThat(result.data.internationalPaymentId).isNotEmpty()
        assertThat(result.data.creationDateTime).isNotNull()
        Assertions.assertThat(result.data.status.toString()).`is`(StatusV4.paymentCondition)
    }

    fun getInternationalPayments_mandatoryFields_Test() {
        // Given
        val consentRequest =
            consentFactory.createConsentWithOnlyMandatoryFieldsPopulated()

        val paymentResponse = createInternationalPayment.submitPayment(consentRequest)

        // When
        val result = getInternationalPayment(paymentResponse)

        // Then
        assertThat(result).isNotNull()
        assertThat(result.data.internationalPaymentId).isNotEmpty()
        assertThat(result.data.creationDateTime).isNotNull()
        assertThat(result.data.charges).isNotNull().isEmpty()
        Assertions.assertThat(result.data.status.toString()).`is`(StatusV4.paymentCondition)
    }

    fun shouldGetInternationalPayments_withReadRefund_Test() {
        // Given
        val consentRequest = consentFactory.createConsent()
        consentRequest.data.readRefundAccount = OBReadRefundAccount.YES

        val paymentResponse = createInternationalPayment.submitPayment(consentRequest)

        // When
        val result = getInternationalPayment(paymentResponse)

        // Then
        assertThat(result).isNotNull()
        assertThat(result.data.internationalPaymentId).isNotEmpty()
        assertThat(result.data.creationDateTime).isNotNull()
        assertThat(result.data.charges).isNotNull().isEmpty()
        assertThat(result.data.refund).isNotNull()
        assertThat(result.data.refund.account).isNotNull()
        Assertions.assertThat(result.data.status.toString()).`is`(StatusV4.paymentCondition)
    }

    private fun getInternationalPayment(paymentResponse: OBWriteInternationalResponse5): OBWriteInternationalResponse5 {
        val getDomesticPaymentUrl = PaymentFactory.urlWithInternationalPaymentId(
            paymentLinks.GetInternationalPayment,
            paymentResponse.data.internationalPaymentId
        )
        return paymentApiClient.sendGetRequest(
            getDomesticPaymentUrl,
            tppResource.tpp.getClientCredentialsAccessToken(defaultPaymentScopesForAccessToken)
        )
    }
}
