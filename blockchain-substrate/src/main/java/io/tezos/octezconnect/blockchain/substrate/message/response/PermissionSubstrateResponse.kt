package io.tezos.octezconnect.blockchain.substrate.message.response

import io.tezos.octezconnect.blockchain.substrate.data.SubstrateAccount
import io.tezos.octezconnect.blockchain.substrate.data.SubstrateAppMetadata
import io.tezos.octezconnect.blockchain.substrate.data.SubstratePermission
import io.tezos.octezconnect.blockchain.substrate.extensions.ownAppMetadata
import io.tezos.octezconnect.blockchain.substrate.message.request.PermissionSubstrateRequest
import io.tezos.octezconnect.core.client.BeaconClient
import io.tezos.octezconnect.core.client.BeaconConsumer
import io.tezos.octezconnect.core.data.Connection
import io.tezos.octezconnect.core.message.PermissionBeaconResponse

public data class PermissionSubstrateResponse internal constructor(
    override val id: String,
    override val version: String,
    override val destination: Connection.Id,
    override val blockchainIdentifier: String,
    public val appMetadata: SubstrateAppMetadata,
    public val scopes: List<SubstratePermission.Scope>,
    public val accounts: List<SubstrateAccount>,
) : PermissionBeaconResponse() {
    public companion object {

        /**
         * Creates a new instance of [PermissionSubstrateResponse] from the [request]
         * with the specified [accounts], [appMetadata] and optional [scopes].
         *
         * The response will have an id matching the one of the [request].
         * If no custom [scopes] are provided, the values will be also taken from the [request].
         */
        public fun from(
            request: PermissionSubstrateRequest,
            accounts: List<SubstrateAccount>,
            appMetadata: SubstrateAppMetadata,
            scopes: List<SubstratePermission.Scope> = request.scopes,
        ): PermissionSubstrateResponse =
            PermissionSubstrateResponse(
                request.id,
                request.version,
                request.origin,
                request.blockchainIdentifier,
                appMetadata,
                scopes,
                accounts,
            )

        /**
         * Creates a new instance of [PermissionSubstrateResponse] from the [request]
         * with the specified [accounts] and optional [scopes]. The [appMetadata] is provided by
         * the [consumer].
         *
         * The response will have an id matching the one of the [request].
         * If no custom [scopes] are provided, the values will be also taken from the [request].
         */
        public fun <T> from(
            request: PermissionSubstrateRequest,
            accounts: List<SubstrateAccount>,
            consumer: T,
            scopes: List<SubstratePermission.Scope> = request.scopes,
        ): PermissionSubstrateResponse where T : BeaconConsumer, T : BeaconClient<*> =
            PermissionSubstrateResponse(
                request.id,
                request.version,
                request.origin,
                request.blockchainIdentifier,
                consumer.ownAppMetadata(),
                scopes,
                accounts,
            )
    }
}
