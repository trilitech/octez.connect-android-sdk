package io.tezos.octezconnect.core.internal.message

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.data.Connection
import io.tezos.octezconnect.core.internal.blockchain.BlockchainRegistry
import io.tezos.octezconnect.core.internal.compat.Compat
import io.tezos.octezconnect.core.internal.compat.VersionedCompat
import io.tezos.octezconnect.core.internal.message.v1.V1BeaconMessage
import io.tezos.octezconnect.core.internal.message.v2.V2BeaconMessage
import io.tezos.octezconnect.core.internal.message.v3.V3BeaconMessage
import io.tezos.octezconnect.core.internal.utils.KJsonSerializer
import io.tezos.octezconnect.core.internal.utils.blockchainRegistry
import io.tezos.octezconnect.core.internal.utils.compat
import io.tezos.octezconnect.core.internal.utils.getString
import io.tezos.octezconnect.core.message.BeaconMessage
import io.tezos.octezconnect.core.scope.BeaconScope
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.jsonObject

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public abstract class VersionedBeaconMessage {
    public abstract val version: String

    public abstract suspend fun toBeaconMessage(origin: Connection.Id, destination: Connection.Id, beaconScope: BeaconScope): BeaconMessage

    public companion object {
        public fun from(senderId: String, message: BeaconMessage, context: Context): VersionedBeaconMessage {
            return when (message.version.major) {
                "1" -> V1BeaconMessage.from(senderId, message, context.toV1())
                "2" -> V2BeaconMessage.from(senderId, message, context.toV2())
                "3" -> V3BeaconMessage.from(senderId, message, context.toV3())

                // fallback to the newest version
                else -> V3BeaconMessage.from(senderId, message, context.toV3())
            }
        }

        private val String.major: String
            get() = substringBefore('.')

        public fun serializer(blockchainRegistry: BlockchainRegistry, compat: Compat<VersionedCompat>): KSerializer<VersionedBeaconMessage> =
            Serializer(blockchainRegistry, compat)

        public fun serializer(beaconScope: BeaconScope? = null): KSerializer<VersionedBeaconMessage> =
            Serializer(beaconScope)
    }

    internal class Serializer(private val blockchainRegistry: BlockchainRegistry, private val compat: Compat<VersionedCompat>) : KJsonSerializer<VersionedBeaconMessage> {
        constructor(beaconScope: BeaconScope? = null) : this(blockchainRegistry(beaconScope), compat(beaconScope))

        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("VersionedBeaconMessage") {
            element<String>("version")
        }

        override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): VersionedBeaconMessage {
            val version = jsonElement.jsonObject.getString(descriptor.getElementName(0))

            return when (version.major) {
                "1" -> jsonDecoder.json.decodeFromJsonElement(V1BeaconMessage.serializer(blockchainRegistry, compat), jsonElement)
                "2" -> jsonDecoder.json.decodeFromJsonElement(V2BeaconMessage.serializer(blockchainRegistry, compat), jsonElement)
                "3" -> jsonDecoder.json.decodeFromJsonElement(V3BeaconMessage.serializer(), jsonElement)

                // fallback to the newest version
                else -> jsonDecoder.json.decodeFromJsonElement(V3BeaconMessage.serializer(), jsonElement)
            }
        }

        override fun serialize(jsonEncoder: JsonEncoder, value: VersionedBeaconMessage) {
            when (value) {
                is V1BeaconMessage -> jsonEncoder.encodeSerializableValue(V1BeaconMessage.serializer(blockchainRegistry, compat), value)
                is V2BeaconMessage -> jsonEncoder.encodeSerializableValue(V2BeaconMessage.serializer(blockchainRegistry, compat), value)
                is V3BeaconMessage -> jsonEncoder.encodeSerializableValue(V3BeaconMessage.serializer(), value)
            }
        }
    }

    public class Context(public val blockchainRegistry: BlockchainRegistry, public val compat: Compat<VersionedCompat>) {
        public fun toV1(): V1BeaconMessage.Context = V1BeaconMessage.Context(compat)
        public fun toV2(): V2BeaconMessage.Context = V2BeaconMessage.Context(compat)
        public fun toV3(): V3BeaconMessage.Context = V3BeaconMessage.Context(blockchainRegistry)
    }
}