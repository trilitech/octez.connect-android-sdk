package io.tezos.octezconnect.blockchain.tezos.extension

import io.tezos.octezconnect.blockchain.tezos.data.TezosAppMetadata
import io.tezos.octezconnect.core.client.BeaconClient

// -- AppMetadata --

public fun <T> T.ownAppMetadata(): TezosAppMetadata where T : BeaconClient<*> =
    TezosAppMetadata(
        senderId = senderId,
        name = app.name,
        icon = app.icon,
    )