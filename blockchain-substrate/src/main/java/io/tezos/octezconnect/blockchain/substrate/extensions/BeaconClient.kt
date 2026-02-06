package io.tezos.octezconnect.blockchain.substrate.extensions

import io.tezos.octezconnect.blockchain.substrate.data.SubstrateAppMetadata
import io.tezos.octezconnect.core.client.BeaconClient

// -- AppMetadata --

public fun <T> T.ownAppMetadata(): SubstrateAppMetadata where T : BeaconClient<*> =
    SubstrateAppMetadata(
        senderId = senderId,
        name = app.name,
        icon = app.icon,
    )