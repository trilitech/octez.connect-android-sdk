package io.tezos.octezconnect.blockchain.tezos.extension

import io.tezos.octezconnect.blockchain.tezos.data.TezosError
import io.tezos.octezconnect.core.message.BlockchainBeaconRequest
import io.tezos.octezconnect.core.message.ErrorBeaconResponse
import io.tezos.octezconnect.core.message.PermissionBeaconRequest

/**
 * Creates a new instance of [ErrorBeaconResponse] from the [request]
 * with the specified [errorType].
 *
 * The response will have an id matching the one of the [request].
 */
public fun ErrorBeaconResponse.Companion.from(request: PermissionBeaconRequest, errorType: TezosError, description: String? = null): ErrorBeaconResponse =
    ErrorBeaconResponse(request.id, request.version, request.origin, errorType, description)

/**
 * Creates a new instance of [ErrorBeaconResponse] from the [request]
 * with the specified [errorType].
 *
 * The response will have an id matching the one of the [request].
 */
public fun ErrorBeaconResponse.Companion.from(request: BlockchainBeaconRequest, errorType: TezosError, description: String? = null): ErrorBeaconResponse =
    ErrorBeaconResponse(request.id, request.version, request.origin, errorType, description)
