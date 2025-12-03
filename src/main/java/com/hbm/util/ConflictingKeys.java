package com.hbm.util;

import net.neoforged.neoforge.client.settings.IKeyConflictContext;

public enum ConflictingKeys implements IKeyConflictContext {
    NO_CONFLICT {
        @Override
        public boolean isActive() { return true; }

        @Override
        public boolean conflicts(IKeyConflictContext iKeyConflictContext) { return false; }
    }
}
