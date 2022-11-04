package com.forgerock.uk.openbanking.tests.functional.payment.file.payments.api.v3_1_8

import assertk.assertThat
import assertk.assertions.isIn
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import com.forgerock.securebanking.framework.conditions.Status
import com.forgerock.securebanking.framework.extensions.junit.CreateTppCallback
import com.forgerock.securebanking.openbanking.uk.common.api.meta.obie.OBVersion
import com.forgerock.uk.openbanking.support.discovery.getPaymentsApiLinks
import com.forgerock.uk.openbanking.support.payment.PaymentFactory
import com.forgerock.uk.openbanking.support.payment.PaymentFileType
import com.forgerock.uk.openbanking.support.payment.defaultPaymentScopesForAccessToken
import org.assertj.core.api.Assertions
import uk.org.openbanking.datamodel.payment.OBWriteFileResponse3

class GetFilePayment(val version: OBVersion, val tppResource: CreateTppCallback.TppResource) {

    private val createFilePaymentApi = CreateFilePayment(version, tppResource)
    private val paymentLinks = getPaymentsApiLinks(version)
    private val paymentApiClient = tppResource.tpp.paymentApiClient

    fun getFilePaymentsTest() {
        // Given
        val fileContent = PaymentFactory.getFileAsString(PaymentFactory.FilePaths.XML_FILE_PATH)

        val consentRequest = PaymentFactory.createOBWriteFileConsent3WithFileInfo(
            fileContent,
            PaymentFileType.UK_OBIE_PAIN_001_001_008.type
        )

        val filePaymentResponse = createFilePaymentApi.submitFilePayment(consentRequest)

        // When
        val getFilePaymentResponse = getFilePayment(filePaymentResponse)

        // Then
        assertThat(getFilePaymentResponse).isNotNull()
        assertThat(getFilePaymentResponse.data.filePaymentId).isNotEmpty()
        assertThat(getFilePaymentResponse.data.creationDateTime).isNotNull()
        assertThat(getFilePaymentResponse.data.charges).isNotNull().isNotEmpty()
        Assertions.assertThat(getFilePaymentResponse.data.status.toString()).`is`(Status.paymentCondition)
    }

    fun getFilePayments_mandatoryFieldsTest() {
        // Given
        val fileContent = PaymentFactory.getFileAsString(PaymentFactory.FilePaths.XML_FILE_PATH)

        val consentRequest = PaymentFactory.createOBWriteFileConsent3WithMandatoryFieldsAndFileInfo(
            fileContent,
            PaymentFileType.UK_OBIE_PAIN_001_001_008.type
        )
        val filePaymentResponse = createFilePaymentApi.submitFilePayment(consentRequest)

        // When
        val getFilePaymentResponse = getFilePayment(filePaymentResponse)

        // Then
        assertThat(getFilePaymentResponse).isNotNull()
        assertThat(getFilePaymentResponse.data).isNotNull()
        assertThat(getFilePaymentResponse.data.consentId).isNotEmpty()
        assertThat(getFilePaymentResponse.data.creationDateTime).isNotNull()
        assertThat(getFilePaymentResponse.data.statusUpdateDateTime).isNotNull()
        assertThat(getFilePaymentResponse.data.initiation.fileHash).isNotNull().isNotEmpty()
        assertThat(getFilePaymentResponse.data.initiation.fileType).isNotNull().isNotEmpty()
        assertThat(getFilePaymentResponse.data.initiation.fileType).isIn(
            PaymentFileType.UK_OBIE_PAIN_001_001_008.type, PaymentFileType.UK_OBIE_PAYMENT_INITIATION_V3_1.type
        )
        assertThat(getFilePaymentResponse.data.filePaymentId).isNotEmpty()
        Assertions.assertThat(getFilePaymentResponse.data.status.toString()).`is`(Status.paymentCondition)

    }


    private fun getFilePayment(filePaymentResponse: OBWriteFileResponse3): OBWriteFileResponse3 {
        val getDomesticPaymentUrl = PaymentFactory.urlWithFilePaymentId(
            paymentLinks.GetFilePayment,
            filePaymentResponse.data.consentId
        )
        return paymentApiClient.sendGetRequest(
            getDomesticPaymentUrl,
            tppResource.tpp.getClientCredentialsAccessToken(defaultPaymentScopesForAccessToken)
        )
    }

}
