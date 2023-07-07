package fr.baldurcrew.gmtk2023;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.github.xpenatan.imgui.core.ImDrawData;
import com.github.xpenatan.imgui.core.ImGui;
import com.github.xpenatan.imgui.core.ImGuiIO;
import com.github.xpenatan.imgui.core.enums.ImGuiConfigFlags;
import com.github.xpenatan.imgui.gdx.ImGuiGdxImpl;
import com.github.xpenatan.imgui.gdx.ImGuiGdxInputMultiplexer;

public class ImGuiWrapper implements Disposable {

    private ImGuiGdxImpl imGuiImpl;

    public ImGuiWrapper() {
        ImGui.init();

        ImGuiIO io = com.github.xpenatan.imgui.core.ImGui.GetIO();
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            // Not possible to have ini filename with webgl
            com.github.xpenatan.imgui.core.ImGui.GetIO().setIniFilename(null);
        }

        io.SetConfigFlags(ImGuiConfigFlags.DockingEnable);

        ImGuiGdxInputMultiplexer input = new ImGuiGdxInputMultiplexer();
        imGuiImpl = new ImGuiGdxImpl();
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void dispose() {
        ImGui.dispose();
    }

    public void render() {
        ImGui.Render();
        ImDrawData drawData = ImGui.GetDrawData();
        imGuiImpl.render(drawData);
    }

    public void prepareRender() {
        imGuiImpl.update();
    }
}
