package io.tezos.octezconnect.core.internal.migration

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.internal.utils.success

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public abstract class VersionedMigration {
    public abstract val fromVersion: String

    public fun migrationIdentifier(target: Migration.Target): String =
        "from_$fromVersion@${target.identifier}"

    public abstract fun targets(target: Migration.Target): Boolean

    public abstract suspend fun perform(target: Migration.Target): Result<Unit>

    protected fun skip(): Result<Unit> = Result.success()
}