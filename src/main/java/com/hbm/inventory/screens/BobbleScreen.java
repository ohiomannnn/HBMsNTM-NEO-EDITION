package com.hbm.inventory.screens;

import com.hbm.blocks.generic.BobbleBlock.BobbleType;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.util.Tuple.Pair;
import com.mojang.blaze3d.vertex.*;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public class BobbleScreen extends Screen {

    private final BobbleType type;

    public BobbleScreen(BobbleType type) {
        super(Component.empty());

        this.type = type;
    }

    @Override
    protected void init() {
        this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(NtmSoundEvents.BOBBLE, 1.0F));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // draws background
        guiGraphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);

        float sizeX = 300;
        float sizeY = 150;
        float left = (this.width - sizeX) / 2;
        float top = (this.height - sizeY) / 2;

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder builder = tess.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        builder.addVertex(left + sizeX, top, 0F).setColor(0F, 0.2F, 0F, 0.8F);
        builder.addVertex(left, top, 0F).setColor(0F, 0.2F, 0F, 0.8F);
        builder.addVertex(left, top + sizeY, 0F).setColor(0F, 0.2F, 0F, 0.8F);
        builder.addVertex(left + sizeX, top + sizeY, 0F).setColor(0F, 0.2F, 0F, 0.8F);
        BufferUploader.drawWithShader(builder.buildOrThrow());

        int nextLevel = (int) (top + 10);

        String bobbleTitle = "Nuclear Tech Commemorative Bobblehead";
        guiGraphics.drawString(this.font, bobbleTitle, (int)(left + sizeX / 2 - this.font.width(bobbleTitle) / 2), nextLevel, 0x00ff00, true);

        nextLevel += 10;

        String bobbleName = this.type.name;
        if(this.type == BobbleType.MELLOW)
            bobbleName = anagramIt(bobbleName, "GEORGEWILLIAMPATON");

        guiGraphics.drawString(this.font, bobbleName, (int)(left + sizeX / 2 - this.font.width(bobbleName) / 2), nextLevel, 0x009900, true);

        nextLevel += 20;

        if(this.type.contribution != null) {

            String title = "Has contributed";
            guiGraphics.drawString(this.font, title, (int)(left + sizeX / 2 - this.font.width(title) / 2), nextLevel, 0x00ff00, true);

            nextLevel += 10;

            String[] list = this.type.contribution.split("\\$");
            for(String text : list) {
                guiGraphics.drawString(this.font, text, (int)(left + sizeX / 2 - this.font.width(text) / 2), nextLevel, 0x009900, true);
                nextLevel += 10;
            }

            nextLevel += 10;
        }

        if(this.type.inscription != null) {

            String title = "On the bottom is the following inscription:";
            guiGraphics.drawString(this.font, title, (int)(left + sizeX / 2 - this.font.width(title) / 2), nextLevel, 0x00ff00, true);

            nextLevel += 10;

            String[] list = this.type.inscription.split("\\$");
            for(String text : list) {
                guiGraphics.drawString(this.font, text, (int)(left + sizeX / 2 - this.font.width(text) / 2), nextLevel, 0x009900, true);
                nextLevel += 10;
            }

            nextLevel += 10;
        }

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // Animates the letters (from -> to) back and forth over 1.5 seconds
    private String anagramIt(String from, String to) {
        double t = Math.sin((double)System.currentTimeMillis() / 1500.0) * 0.75 + 0.5;

        char[] lettersFrom = from.toCharArray();
        char[] lettersTo = to.toCharArray();
        boolean[] hasPairedLetter = new boolean[lettersFrom.length];
        List<Pair<Double, Character>> letterTargets = new ArrayList<>();

        for(int i = 0; i < lettersFrom.length; i++) {
            char letterFrom = lettersFrom[i];
            for(int o = 0; o < lettersTo.length; o++) {
                char letterTo = lettersTo[o];
                if(letterFrom == letterTo && !hasPairedLetter[o]) {
                    double v = lerp(i, o, t);
                    letterTargets.add(new Pair<>(v, lettersFrom[i]));
                    hasPairedLetter[o] = true;
                    break;
                }
            }
        }

        for(int i = 0; i < letterTargets.size(); i++) {
            for (int j = i + 1; j < letterTargets.size(); j++) {
                if (letterTargets.get(i).key > letterTargets.get(j).key) {
                    Pair<Double, Character> temp = letterTargets.get(i);
                    letterTargets.set(i, letterTargets.get(j));
                    letterTargets.set(j, temp);
                }
            }
        }

        String anagrammedText = "";
        for(Pair<Double, Character> in : letterTargets) {
            anagrammedText += in.value;
        }

        return anagrammedText;
    }

    private double lerp(double a, double b, double t) {
        t = Math.clamp(t, 0, 1);
        return a * (1 - t) + b * t;
    }
}
