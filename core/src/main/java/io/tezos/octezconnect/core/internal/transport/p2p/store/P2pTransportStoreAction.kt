package io.tezos.octezconnect.core.internal.transport.p2p.store

import io.tezos.octezconnect.core.data.P2pPeer

internal sealed interface P2pTransportStoreAction

internal object OnPairingRequested : P2pTransportStoreAction
internal data class OnPairingCompleted(val peer: P2pPeer) : P2pTransportStoreAction
internal object DiscardPairingData : P2pTransportStoreAction