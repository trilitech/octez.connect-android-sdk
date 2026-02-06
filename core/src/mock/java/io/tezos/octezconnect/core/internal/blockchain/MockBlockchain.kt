package io.tezos.octezconnect.core.internal.blockchain

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.blockchain.Blockchain
import io.tezos.octezconnect.core.internal.di.DependencyRegistry
import io.tezos.octezconnect.core.internal.utils.IdentifierCreator

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class MockBlockchain : Blockchain {
    override val identifier: String = IDENTIFIER
    override val creator: Blockchain.Creator = MockBlockchainCreator()
    override val serializer: Blockchain.Serializer = MockBlockchainSerializer()

    public class Factory : Blockchain.Factory<MockBlockchain> {
        override val identifier: String = IDENTIFIER
        override fun create(dependencyRegistry: DependencyRegistry): MockBlockchain = MockBlockchain()
    }

    public companion object {
        public const val IDENTIFIER: String = "mockBlockchain"
    }
}
