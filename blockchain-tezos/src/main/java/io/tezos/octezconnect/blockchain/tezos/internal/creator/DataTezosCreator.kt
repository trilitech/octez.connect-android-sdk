package io.tezos.octezconnect.blockchain.tezos.internal.creator

import io.tezos.octezconnect.blockchain.tezos.data.TezosAppMetadata
import io.tezos.octezconnect.blockchain.tezos.data.TezosPermission
import io.tezos.octezconnect.blockchain.tezos.internal.utils.failWithUnknownMessage
import io.tezos.octezconnect.blockchain.tezos.message.request.PermissionTezosRequest
import io.tezos.octezconnect.blockchain.tezos.message.response.PermissionTezosResponse
import io.tezos.octezconnect.core.data.Account
import io.tezos.octezconnect.core.data.Connection
import io.tezos.octezconnect.core.data.Permission
import io.tezos.octezconnect.core.internal.blockchain.creator.DataBlockchainCreator
import io.tezos.octezconnect.core.internal.storage.StorageManager
import io.tezos.octezconnect.core.internal.utils.IdentifierCreator
import io.tezos.octezconnect.core.internal.utils.asHexString
import io.tezos.octezconnect.core.internal.utils.currentTimestamp
import io.tezos.octezconnect.core.internal.utils.failWithIllegalState
import io.tezos.octezconnect.core.message.PermissionBeaconRequest
import io.tezos.octezconnect.core.message.PermissionBeaconResponse
import io.tezos.octezconnect.core.storage.findAppMetadata

internal class DataTezosCreator(
    private val storageManager: StorageManager,
    private val identifierCreator: IdentifierCreator,
) : DataBlockchainCreator {
    override suspend fun extractIncomingPermission(request: PermissionBeaconRequest, response: PermissionBeaconResponse, origin: Connection.Id): Result<List<Permission>> =
        runCatching {
            if (request !is PermissionTezosRequest) failWithUnknownMessage(request)
            if (response !is PermissionTezosResponse) failWithUnknownMessage(response)

            val peer = storageManager.findPeer { it.publicKey == origin.id } ?: failWithAppMetadataNotFound()
            val senderId = identifierCreator.senderId(origin.id.asHexString().toByteArray()).getOrThrow()
            val appMetadata = TezosAppMetadata(senderId, peer.name, peer.icon)

            listOf(
                TezosPermission(
                    response.account.accountId,
                    senderId,
                    connectedAt = currentTimestamp(),
                    response.account.address,
                    response.account.publicKey,
                    response.account.network,
                    appMetadata,
                    response.scopes,
                ),
            )
        }

    override suspend fun extractOutgoingPermission(request: PermissionBeaconRequest, response: PermissionBeaconResponse): Result<List<Permission>> =
        runCatching {
            if (request !is PermissionTezosRequest) failWithUnknownMessage(request)
            if (response !is PermissionTezosResponse) failWithUnknownMessage(response)

            val appMetadata = storageManager.findAppMetadata<TezosAppMetadata> { it.senderId == request.senderId } ?: failWithAppMetadataNotFound()
            val senderId = identifierCreator.senderId(request.origin.id.asHexString().toByteArray()).getOrThrow()

            listOf(
                TezosPermission(
                    response.account.accountId,
                    senderId,
                    connectedAt = currentTimestamp(),
                    response.account.address,
                    response.account.publicKey,
                    response.account.network,
                    appMetadata,
                    response.scopes,
                ),
            )
        }

    override fun extractAccounts(response: PermissionBeaconResponse): Result<List<Account>> =
        runCatching {
            if (response !is PermissionTezosResponse) failWithUnknownMessage(response)

            listOf(Account(response.account.accountId, response.account.address))
        }

    private fun failWithAppMetadataNotFound(): Nothing = failWithIllegalState("Permission could not be extracted, matching appMetadata not found.")
}