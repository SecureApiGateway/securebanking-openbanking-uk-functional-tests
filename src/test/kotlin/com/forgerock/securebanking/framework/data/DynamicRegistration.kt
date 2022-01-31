package com.forgerock.securebanking.framework.data

import com.forgerock.openbanking.constants.OpenBankingConstants
import com.forgerock.securebanking.framework.constants.OB_SOFTWARE_ID
import com.forgerock.securebanking.framework.constants.REDIRECT_URI
import com.forgerock.securebanking.support.discovery.asDiscovery

data class RegistrationRequest(
    val iss: String = OB_SOFTWARE_ID,
    val exp: Long = (System.currentTimeMillis() / 1000) + 180,
    val grant_types: List<String> = asDiscovery.grant_types_supported,
    val id_token_signed_response_alg: String = "PS256",
    val redirect_uris: List<String> = listOf(REDIRECT_URI),
    val request_object_encryption_alg: String = "RSA-OAEP-256",
    val request_object_encryption_enc: String? = asDiscovery.request_object_encryption_enc_values_supported[0],
    val request_object_signing_alg: String = "PS256",
    val response_types: List<String> = listOf("code id_token"),
    val scope: String = asDiscovery.scopes_supported.intersect(
        listOf(
            OpenBankingConstants.Scope.OPENID,
            OpenBankingConstants.Scope.ACCOUNTS,
            OpenBankingConstants.Scope.PAYMENTS,
            OpenBankingConstants.Scope.FUNDS_CONFIRMATIONS,
            OpenBankingConstants.Scope.EVENT_POLLING
        )
    ).joinToString(separator = " "),
    val software_statement: String,
    val subject_type: String = "pairwise",
    val token_endpoint_auth_method: String = "client_secret_basic",
    val token_endpoint_auth_signing_alg: String = "PS256",
    val tls_client_auth_subject_dn: String? = null
)

data class RegistrationResponse(
    val application_type: String,
    val client_id: String,
    val client_secret: String? = null,
    val client_secret_expires_at: String? = null,
    val default_max_age: String,
    val grant_types: List<String>,
    val id_token_encrypted_response_alg: String,
    val id_token_encrypted_response_enc: String,
    val id_token_signed_response_alg: String,
    val jwks_uri: String,
    val redirect_uris: List<String>,
    val registration_access_token: String,
    val registration_client_uri: String,
    val request_object_encryption_alg: String,
    val request_object_encryption_enc: String?,
    val request_object_signing_alg: String,
    val response_types: List<String>,
    val scope: String,
    val scopes: List<String>,
    val subject_type: String,
    val token_endpoint_auth_method: String,
    val token_endpoint_auth_signing_alg: String,
    val userinfo_encrypted_response_alg: String,
    val userinfo_encrypted_response_enc: String,
    val userinfo_signed_response_alg: String,
    val introspection_encrypted_response_alg: String,
    val introspection_encrypted_response_enc: String,
    val introspection_signed_response_alg: String,
    val client_type: String,
    val public_key_selector: String,
    val authorization_code_lifetime: Long,
    val user_info_response_format_selector: String,
    val tls_client_certificate_bound_access_tokens: Boolean,
    val backchannel_logout_session_required: Boolean,
    val client_name: String,
    val default_max_age_enabled: Boolean,
    val token_intro_response_format_selector: String,
    val jwt_token_lifetime: Long,
    val id_token_encryption_enabled: Boolean,
    val access_token_lifetime: Long,
    val refresh_token_lifetime: Long,
    val software_statement: String? = null
)
