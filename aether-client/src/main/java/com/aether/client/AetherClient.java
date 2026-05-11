package com.aether.client;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.awt.*;
import java.nio.*;

import com.aether.common.PlayerMovePacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;


import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;


public class AetherClient {


    public class GameWindow {
        private long window;

        public void run(){
            init();
            loop();

            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
            glfwTerminate();
        }

        private void init(){
            if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

            // Configure GLFW
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

            // Create the window
            window = glfwCreateWindow(800, 600, "AetherNet Engine", NULL, NULL);
            if (window == NULL) throw new RuntimeException("Failed to create the GLFW window");

            // Center the window on your screen
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (vidmode.width() - 800) / 2, (vidmode.height() - 600) / 2);

            glfwMakeContextCurrent(window);
            glfwSwapInterval(1); // Enable v-sync (prevents screen tearing)
            glfwShowWindow(window);

            GL.createCapabilities();
        }

        private void loop() {
            // This is the heart of your game's "Eyes"
            while (!glfwWindowShouldClose(window)) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the screen
                glClearColor(0.1f, 0.1f, 0.2f, 1.0f); // Set a dark blue background

                // Future: Draw players here!

                glfwSwapBuffers(window); // Swap the color buffers (show the frame)
                glfwPollEvents(); // Check for key presses/mouse clicks
            }
        }

    }



    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 40000;

        // 1. BACKGROUND THREAD: Networking (The Brain)
        new Thread(() -> {
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ClientInitalizer());

                System.out.println("Aether Client is connecting to " + host + ":" + port + "...");
                ChannelFuture future = bootstrap.connect(host, port).sync();
                System.out.println("Aether Client connected!");

                future.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                group.shutdownGracefully();
            }
        }).start();

        // 2. MAIN THREAD: Graphics (The Eyes)
        // This must stay on the main thread or GLFW will crash.
        // It also must be LAST because .run() is an infinite loop.
        System.out.println("Launching Game Window...");
        new AetherClient().new GameWindow().run();
    }

}
