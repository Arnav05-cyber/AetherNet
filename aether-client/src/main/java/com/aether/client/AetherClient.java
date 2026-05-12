package com.aether.client;

import com.aether.common.PlayerMovePacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class AetherClient {

    private final ClientWorld world = new ClientWorld();
    private Channel channel;
    private String localPlayerId;

    // Movement state
    private float myX = 0.0f;
    private float myY = 0.0f;

    public class GameWindow {
        private long window;

        public void run() {
            init();
            loop();
            cleanup();
        }

        private void init() {
            if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

            window = glfwCreateWindow(800, 600, "AetherNet Engine", NULL, NULL);
            if (window == NULL) throw new RuntimeException("Failed to create window");

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (vidmode != null) {
                glfwSetWindowPos(window, (vidmode.width() - 800) / 2, (vidmode.height() - 600) / 2);
            }

            glfwMakeContextCurrent(window);
            glfwSwapInterval(1);
            glfwShowWindow(window);
            GL.createCapabilities();
        }

        private void loop() {
            while (!glfwWindowShouldClose(window)) {
                // 1. Handle Input
                handleInput();

                // 2. Clear Screen
                glClearColor(0.1f, 0.1f, 0.2f, 1.0f);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                // 3. Draw All Players
                for (var entry : world.getPlayerPositions().entrySet()) {
                    Vector2f pos = entry.getValue();
                    // Local player is Green, others are White
                    if (entry.getKey().equals(localPlayerId)) {
                        drawPlayerSquare(pos.x, pos.y, 0.0f, 1.0f, 0.0f);
                    } else {
                        drawPlayerSquare(pos.x, pos.y, 1.0f, 1.0f, 1.0f);
                    }
                }

                glfwSwapBuffers(window);
                glfwPollEvents();
            }
        }

        private void handleInput() {
            float speed = 0.01f;
            boolean moved = false;

            if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) { myY += speed; moved = true; }
            if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) { myY -= speed; moved = true; }
            if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) { myX -= speed; moved = true; }
            if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) { myX += speed; moved = true; }

            // If we moved locally, send the update to the server
            if (moved && channel != null && localPlayerId != null) {
                channel.writeAndFlush(new PlayerMovePacket(myX, myY, localPlayerId));                // Predict locally so it feels smooth
                world.updatePlayer(myX, myY, localPlayerId);
            }
        }

        private void drawPlayerSquare(float x, float y, float r, float g, float b) {
            glBegin(GL_QUADS);
            glColor3f(r, g, b);
            glVertex2f(x - 0.05f, y - 0.05f);
            glVertex2f(x + 0.05f, y - 0.05f);
            glVertex2f(x + 0.05f, y + 0.05f);
            glVertex2f(x - 0.05f, y + 0.05f);
            glEnd();
        }

        private void cleanup() {
            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
            glfwTerminate();
        }
    }

    // Setters for the Network thread to call
    public void setLocalPlayerId(String id) { this.localPlayerId = id; }
    public void setChannel(Channel ch) { this.channel = ch; }

    public static void main(String[] args) {
        AetherClient client = new AetherClient();

        // Start Networking
        new Thread(() -> {
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(group).channel(NioSocketChannel.class)
                        .handler(new ClientInitalizer(client.world, client));

                ChannelFuture f = b.connect("127.0.0.1", 40000).sync();
                client.setChannel(f.channel());
                f.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                group.shutdownGracefully();
            }
        }).start();

        // Start Graphics
        client.new GameWindow().run();
    }
}