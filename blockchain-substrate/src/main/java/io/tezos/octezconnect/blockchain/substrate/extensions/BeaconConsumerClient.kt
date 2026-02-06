package io.tezos.octezconnect.blockchain.substrate.extensions

import io.tezos.octezconnect.blockchain.substrate.data.SubstrateAccount
import io.tezos.octezconnect.blockchain.substrate.data.SubstratePermission
import io.tezos.octezconnect.blockchain.substrate.message.request.PermissionSubstrateRequest
import io.tezos.octezconnect.blockchain.substrate.message.request.SignPayloadSubstrateRequest
import io.tezos.octezconnect.blockchain.substrate.message.request.TransferSubstrateRequest
import io.tezos.octezconnect.blockchain.substrate.message.response.PermissionSubstrateResponse
import io.tezos.octezconnect.blockchain.substrate.message.response.SignPayloadSubstrateResponse
import io.tezos.octezconnect.blockchain.substrate.message.response.TransferSubstrateResponse
import io.tezos.octezconnect.core.client.BeaconClient
import io.tezos.octezconnect.core.client.BeaconConsumer

// -- response --

public suspend fun <T> T.respondToSubstratePermission(
    request: PermissionSubstrateRequest,
    accounts: List<SubstrateAccount>,
    scopes: List<SubstratePermission.Scope> = request.scopes
) where T : BeaconConsumer, T : BeaconClient<*> {
    val response = PermissionSubstrateResponse.from(request, accounts, this, scopes)
    respond(response)
}

public suspend fun <T> T.respondToSubstrateTransferRequest(
    request: TransferSubstrateRequest,
    transactionHash: String?,
    signature: String?,
    payload: String? = null,
) where T : BeaconConsumer, T : BeaconClient<*> {
    val response = TransferSubstrateResponse.from(request, transactionHash, signature, payload)
    respond(response)
}

public suspend fun <T> T.respondToSubstrateTransferRequest(
    request: TransferSubstrateRequest.Submit,
    transactionHash: String,
) where T : BeaconConsumer, T : BeaconClient<*> {
    val response = TransferSubstrateResponse.from(request, transactionHash)
    respond(response)
}

public suspend fun <T> T.respondToSubstrateTransferRequest(
    request: TransferSubstrateRequest.SubmitAndReturn,
    transactionHash: String,
    signature: String,
    payload: String? = null,
) where T : BeaconConsumer, T : BeaconClient<*> {
    val response = TransferSubstrateResponse.from(request, transactionHash, signature, payload)
    respond(response)
}

public suspend fun <T> T.respondToSubstrateTransferRequest(
    request: TransferSubstrateRequest.Return,
    signature: String,
    payload: String? = null,
) where T : BeaconConsumer, T : BeaconClient<*> {
    val response = TransferSubstrateResponse.from(request, signature, payload)
    respond(response)
}

public suspend fun <T> T.respondToSubstrateSignPayloadRequest(
    request: SignPayloadSubstrateRequest,
    transactionHash: String?,
    signature: String?,
    payload: String? = null,
) where T : BeaconConsumer, T : BeaconClient<*> {
    val response = SignPayloadSubstrateResponse.from(request, transactionHash, signature, payload)
    respond(response)
}

public suspend fun <T> T.respondToSubstrateSignPayloadRequest(
    request: SignPayloadSubstrateRequest.Submit,
    transactionHash: String,
) where T : BeaconConsumer, T : BeaconClient<*> {
    val response = SignPayloadSubstrateResponse.from(request, transactionHash)
    respond(response)
}

public suspend fun <T> T.respondToSubstrateSignPayloadRequest(
    request: SignPayloadSubstrateRequest.SubmitAndReturn,
    transactionHash: String,
    signature: String,
    payload: String? = null,
) where T : BeaconConsumer, T : BeaconClient<*> {
    val response = SignPayloadSubstrateResponse.from(request, transactionHash, signature, payload)
    respond(response)
}

public suspend fun <T> T.respondToSubstrateSignPayloadRequest(
    request: SignPayloadSubstrateRequest.Return,
    signature: String,
    payload: String? = null,
) where T : BeaconConsumer, T : BeaconClient<*> {
    val response = SignPayloadSubstrateResponse.from(request, signature, payload)
    respond(response)
}

