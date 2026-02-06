package io.tezos.octezconnect.blockchain.tezos.extension

import io.tezos.octezconnect.blockchain.tezos.data.TezosAccount
import io.tezos.octezconnect.blockchain.tezos.data.TezosAppMetadata
import io.tezos.octezconnect.blockchain.tezos.data.TezosNotification
import io.tezos.octezconnect.blockchain.tezos.data.TezosPermission
import io.tezos.octezconnect.blockchain.tezos.data.TezosThreshold
import io.tezos.octezconnect.blockchain.tezos.message.request.BroadcastTezosRequest
import io.tezos.octezconnect.blockchain.tezos.message.request.OperationTezosRequest
import io.tezos.octezconnect.blockchain.tezos.message.request.PermissionTezosRequest
import io.tezos.octezconnect.blockchain.tezos.message.request.SignPayloadTezosRequest
import io.tezos.octezconnect.blockchain.tezos.message.response.BroadcastTezosResponse
import io.tezos.octezconnect.blockchain.tezos.message.response.OperationTezosResponse
import io.tezos.octezconnect.blockchain.tezos.message.response.PermissionTezosResponse
import io.tezos.octezconnect.blockchain.tezos.message.response.SignPayloadTezosResponse
import io.tezos.octezconnect.core.client.BeaconClient
import io.tezos.octezconnect.core.client.BeaconConsumer
import io.tezos.octezconnect.core.data.SigningType

// -- response --

public suspend fun <T> T.respondToTezosPermission(
    request: PermissionTezosRequest,
    account: TezosAccount,
    scopes: List<TezosPermission.Scope> = request.scopes,
    threshold: TezosThreshold? = null,
    notification: TezosNotification? = null

) where T : BeaconConsumer, T : BeaconClient<*> {
    val response = PermissionTezosResponse.from(request, account, this, scopes, threshold, notification)
    respond(response)
}

public suspend fun <T> T.respondToTezosOperation(
    request: OperationTezosRequest,
    transactionHash: String,
) where T : BeaconConsumer, T : BeaconClient<*> {
    val response = OperationTezosResponse.from(request, transactionHash)
    respond(response)
}

public suspend fun <T> T.respondToTezosSignPayload(
    request: SignPayloadTezosRequest,
    signingType: SigningType,
    signature: String,
) where T : BeaconConsumer, T : BeaconClient<*> {
    val response = SignPayloadTezosResponse.from(request, signingType, signature)
    respond(response)
}

public suspend fun <T> T.respondToTezosBroadcast(
    request: BroadcastTezosRequest,
    transactionHash: String,
) where T : BeaconConsumer, T : BeaconClient<*> {
    val response = BroadcastTezosResponse.from(request, transactionHash)
    respond(response)
}