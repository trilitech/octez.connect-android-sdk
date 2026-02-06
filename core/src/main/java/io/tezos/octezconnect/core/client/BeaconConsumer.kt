package io.tezos.octezconnect.core.client

import io.tezos.octezconnect.core.exception.BeaconException
import io.tezos.octezconnect.core.message.BeaconResponse
import io.tezos.octezconnect.core.transport.data.PairingRequest
import io.tezos.octezconnect.core.transport.data.PairingResponse

public interface BeaconConsumer {
    public val senderId: String
    /**
     * Sends the [response] in reply to a previously received request.
     *
     * @throws [BeaconException] if processing and sending the [response] failed.
     */
    @Throws(BeaconException::class)
    public suspend fun respond(response: BeaconResponse)

    /**
     * Responds to the pairing [request] and finalizes the process. Returns
     * the pairing response, which, depending on the transport used, may require additional handling.
     *
     * @throws [BeaconException] if the process failed.
     */
    @Throws(BeaconException::class)
    public suspend fun pair(request: PairingRequest): PairingResponse

    /**
     * Responds to the pairing [request] and finalizes the process. Returns
     * the pairing response, which, depending on the transport used, may require additional handling.
     *
     * @throws [BeaconException] if the process failed.
     */
    public suspend fun pair(request: String): PairingResponse
}