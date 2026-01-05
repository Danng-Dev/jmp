package top.archiem.jmp.util;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.block.data.type.TechnicalPiston;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import top.archiem.jmp.JMP;

public class TitleHandler {
    private Player player;
    private final TextFormat tfInstance = new TextFormat(JMP.getPlugin(JMP.class).papienabled);

    public TitleHandler(Player player){
        this.player = player;
    }

    public Title titleBuilder(String title, String subtitle, @Nullable Times times){
        Title titleinbuild = new Title() {
            @Override
            public @NotNull Component title() {
                return tfInstance.format(title, player);
            }

            @Override
            public @NotNull Component subtitle() {
                return tfInstance.format(subtitle, player);
            }

            @Override
            public @Nullable Times times() {
                if(times != null){
                    return times;
                }
                return null;
            }

            @Override
            public <T> @UnknownNullability T part(@NotNull TitlePart<T> part) {
                return null;
            }
        };
        return titleinbuild;
    }

}
