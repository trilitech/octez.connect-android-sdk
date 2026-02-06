package io.tezos.octezconnect.blockchain.substrate

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.blockchain.substrate.internal.di.ExtendedDependencyRegistry
import io.tezos.octezconnect.blockchain.substrate.internal.di.extend
import io.tezos.octezconnect.core.blockchain.Blockchain
import io.tezos.octezconnect.core.internal.di.DependencyRegistry

/**
 * Substrate implementation of the [Blockchain] interface.
 */
public class Substrate internal constructor(
    @get:RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) override val creator: Blockchain.Creator,
    @get:RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) override val serializer: Blockchain.Serializer,
) : Blockchain {
    @get:RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    override val identifier: String = IDENTIFIER

    /**
     * Factory for [Substrate].
     *
     * @constructor Creates a factory required for dynamic [Substrate] blockchain registration.
     */
    public class Factory : Blockchain.Factory<Substrate> {
        override val identifier: String = IDENTIFIER

        private var _extendedDependencyRegistry: ExtendedDependencyRegistry? = null
        private fun extendedDependencyRegistry(dependencyRegistry: DependencyRegistry): ExtendedDependencyRegistry =
            _extendedDependencyRegistry ?: dependencyRegistry.extend().also { _extendedDependencyRegistry = it }

        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
        override fun create(dependencyRegistry: DependencyRegistry): Substrate =
            extendedDependencyRegistry(dependencyRegistry).substrate

    }

    public companion object {
        internal const val IDENTIFIER = "substrate"
    }
}

/**
 * Creates a new instance of [Substrate.Factory].
 */
public fun substrate(): Substrate.Factory = Substrate.Factory()