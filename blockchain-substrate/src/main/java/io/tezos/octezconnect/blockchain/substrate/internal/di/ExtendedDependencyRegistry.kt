package io.tezos.octezconnect.blockchain.substrate.internal.di

import io.tezos.octezconnect.blockchain.substrate.Substrate
import io.tezos.octezconnect.blockchain.substrate.internal.creator.SubstrateCreator
import io.tezos.octezconnect.blockchain.substrate.internal.serializer.SubstrateSerializer
import io.tezos.octezconnect.core.internal.di.DependencyRegistry
import io.tezos.octezconnect.core.internal.di.findExtended

internal interface ExtendedDependencyRegistry : DependencyRegistry {

    // -- blockchain --

    val substrate: Substrate

    // -- creator --

    val substrateCreator: SubstrateCreator

    // -- serializer --

    val substrateSerializer: SubstrateSerializer
}

internal fun DependencyRegistry.extend(): ExtendedDependencyRegistry =
    if (this is ExtendedDependencyRegistry) this
    else findExtended<SubstrateDependencyRegistry>() ?: SubstrateDependencyRegistry(this).also { addExtended(it) }