package io.tezos.octezconnect.blockchain.substrate.data

import io.tezos.octezconnect.core.data.BeaconError
import kotlinx.serialization.Serializable

/**
 * Types of Substrate errors supported in Beacon
 */
@Serializable
public sealed class SubstrateError : BeaconError() {

    public companion object {}
}