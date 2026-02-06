package io.tezos.octezconnect.core.internal.transport.p2p.store

import io.tezos.octezconnect.core.data.P2pPeer
import kotlinx.coroutines.CompletableDeferred

internal data class P2pTransportStoreState(
    val pairingPeerDeferred: CompletableDeferred<P2pPeer>? = null
)
