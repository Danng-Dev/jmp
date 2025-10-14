class TextFormatTest {  
      
    @Test  
    void testFormatWithoutPlaceholderAPI() {  
        // Given  
        textFormat formatter = new textFormat(false);  
        Player mockPlayer = mock(Player.class);  
        when(mockPlayer.getName()).thenReturn("TestPlayer");  
          
        // When  
        Component result = formatter.format("&aWelcome %player%!", mockPlayer);  
          
        // Then  
        assertNotNull(result);  
        // Verify the player name replacement works  
    }  
      
    @Test  
    void testFormatWithPlaceholderAPI() {  
        // This requires mocking PlaceholderAPI static methods  
        try (MockedStatic<PlaceholderAPI> papiMock = mockStatic(PlaceholderAPI.class)) {  
            textFormat formatter = new textFormat(true);  
            Player mockPlayer = mock(Player.class);  
              
            papiMock.when(() -> PlaceholderAPI.setPlaceholders(any(), anyString()))  
                   .thenAnswer(invocation -> invocation.getArgument(1));  
              
            Component result = formatter.format("Test message", mockPlayer);  
              
            assertNotNull(result);  
            papiMock.verify(() -> PlaceholderAPI.setPlaceholders(any(), anyString()));  
        }  
    }  
}
