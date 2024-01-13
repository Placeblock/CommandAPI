package de.codelix.commandapi.minecraft;

import de.codelix.commandapi.core.Permission;
import lombok.Getter;

@Getter
public record MinecraftPermission(String permission) implements Permission {
}
