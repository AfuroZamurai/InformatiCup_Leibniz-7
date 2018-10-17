package gui;

import java.awt.image.BufferedImage;

import main.IModule;

public class TestModule implements IModule {

	@Override
	public BufferedImage generateImage(BufferedImage input) {
		
		return input;
	}

}
