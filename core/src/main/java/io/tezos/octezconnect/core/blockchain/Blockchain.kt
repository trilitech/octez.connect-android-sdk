package io.tezos.octezconnect.core.blockchain

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.data.Permission
import io.tezos.octezconnect.core.internal.blockchain.creator.DataBlockchainCreator
import io.tezos.octezconnect.core.internal.blockchain.creator.V1BeaconMessageBlockchainCreator
import io.tezos.octezconnect.core.internal.blockchain.creator.V2BeaconMessageBlockchainCreator
import io.tezos.octezconnect.core.internal.blockchain.creator.V3BeaconMessageBlockchainCreator
import io.tezos.octezconnect.core.internal.blockchain.serializer.DataBlockchainSerializer
import io.tezos.octezconnect.core.internal.blockchain.serializer.V1BeaconMessageBlockchainSerializer
import io.tezos.octezconnect.core.internal.blockchain.serializer.V2BeaconMessageBlockchainSerializer
import io.tezos.octezconnect.core.internal.blockchain.serializer.V3BeaconMessageBlockchainSerializer
import io.tezos.octezconnect.core.internal.di.DependencyRegistry
import io.tezos.octezconnect.core.internal.message.v1.V1BeaconMessage
import io.tezos.octezconnect.core.internal.message.v2.V2BeaconMessage
import io.tezos.octezconnect.core.internal.message.v3.V3BeaconMessage
import io.tezos.octezconnect.core.message.BeaconMessage
import io.tezos.octezconnect.core.message.PermissionBeaconRequest
import io.tezos.octezconnect.core.message.PermissionBeaconResponse
import kotlinx.serialization.KSerializer

public interface Blockchain {
    @get:RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public val identifier: String

    @get:RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public val creator: Creator

    @get:RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public val serializer: Serializer

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public interface Creator {

        // -- data --

        public val data: DataBlockchainCreator

        // -- VersionedBeaconMessage --

        public val v1: V1BeaconMessageBlockchainCreator
        public val v2: V2BeaconMessageBlockchainCreator
        public val v3: V3BeaconMessageBlockchainCreator
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public interface Serializer {

        // -- data --

        public val data: DataBlockchainSerializer

        // -- VersionedBeaconMessage --

        public val v1: V1BeaconMessageBlockchainSerializer
        public val v2: V2BeaconMessageBlockchainSerializer
        public val v3: V3BeaconMessageBlockchainSerializer
    }

    public interface Factory<T : Blockchain> {
        public val identifier: String

        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
        public fun create(dependencyRegistry: DependencyRegistry): T
    }
}
