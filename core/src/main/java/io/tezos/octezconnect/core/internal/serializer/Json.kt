package io.tezos.octezconnect.core.internal.serializer

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.data.AppMetadata
import io.tezos.octezconnect.core.data.BeaconError
import io.tezos.octezconnect.core.data.Network
import io.tezos.octezconnect.core.data.Permission
import io.tezos.octezconnect.core.internal.blockchain.BlockchainRegistry
import io.tezos.octezconnect.core.internal.compat.Compat
import io.tezos.octezconnect.core.internal.compat.VersionedCompat
import io.tezos.octezconnect.core.internal.message.VersionedBeaconMessage
import io.tezos.octezconnect.core.internal.message.v1.ErrorV1BeaconResponse
import io.tezos.octezconnect.core.internal.message.v1.V1BeaconMessage
import io.tezos.octezconnect.core.internal.message.v2.ErrorV2BeaconResponse
import io.tezos.octezconnect.core.internal.message.v2.V2BeaconMessage
import io.tezos.octezconnect.core.internal.message.v3.*
import io.tezos.octezconnect.core.transport.data.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic

private fun serializersModule(
    blockchainRegistry: BlockchainRegistry,
    compat: Compat<VersionedCompat>,
): SerializersModule = SerializersModule {

    // -- data --

    contextual(AppMetadata.Serializer(blockchainRegistry, compat))
    contextual(BeaconError.Serializer(blockchainRegistry))
    contextual(Network.Serializer(blockchainRegistry))
    contextual(Permission.Serializer(blockchainRegistry, compat))

    // -- message versioned --

    contextual(VersionedBeaconMessage.Serializer(blockchainRegistry, compat))

    // -- message v1 --

    contextual(V1BeaconMessage.Serializer(blockchainRegistry, compat))
    contextual(ErrorV1BeaconResponse.Serializer(blockchainRegistry, compat))

    // -- message v2 --

    contextual(V2BeaconMessage.Serializer(blockchainRegistry, compat))
    contextual(ErrorV2BeaconResponse.Serializer(blockchainRegistry, compat))

    // -- message v3 --

    polymorphic(V3BeaconMessage.Content::class) {
        subclass(PermissionV3BeaconRequestContent::class, PermissionV3BeaconRequestContent.serializer(blockchainRegistry))
        subclass(BlockchainV3BeaconRequestContent::class, BlockchainV3BeaconRequestContent.serializer(blockchainRegistry))

        subclass(PermissionV3BeaconResponseContent::class, PermissionV3BeaconResponseContent.serializer(blockchainRegistry))
        subclass(BlockchainV3BeaconResponseContent::class, BlockchainV3BeaconResponseContent.serializer(blockchainRegistry))
        subclass(AcknowledgeV3BeaconResponseContent::class, AcknowledgeV3BeaconResponseContent.serializer())
        subclass(ErrorV3BeaconResponseContent::class, ErrorV3BeaconResponseContent.serializer(blockchainRegistry))

        subclass(DisconnectV3BeaconMessageContent::class, DisconnectV3BeaconMessageContent.serializer())
    }

    contextual(PermissionV3BeaconRequestContent.serializer(blockchainRegistry))
    contextual(PermissionV3BeaconResponseContent.serializer(blockchainRegistry))

    contextual(BlockchainV3BeaconRequestContent.serializer(blockchainRegistry))
    contextual(BlockchainV3BeaconResponseContent.serializer(blockchainRegistry))

    contextual(ErrorV3BeaconResponseContent.serializer(blockchainRegistry))

    // -- pairing --

    contextual(PairingMessage::class, PairingMessage.serializer())
    contextual(PairingRequest::class, PairingRequest.serializer())
    contextual(PairingResponse::class, PairingResponse.serializer())
}

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public const val BEACON_CORE_CLASS_DISCRIMINATOR: String = "_serializationType"

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public fun coreJson(blockchainRegistry: BlockchainRegistry, compat: Compat<VersionedCompat>): Json =
    Json {
        serializersModule = serializersModule(blockchainRegistry, compat)
        classDiscriminator = BEACON_CORE_CLASS_DISCRIMINATOR
        ignoreUnknownKeys = true
    }