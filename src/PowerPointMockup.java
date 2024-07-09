import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.awt.event.*;

public class PowerPointMockup extends JFrame {

    private JPanel slideNavigator;
    private JPanel mainPanel;
    private File[] files;
    private int currentSlideIndex = 0;

    public PowerPointMockup() {
        setTitle("Mockup PowerPoint");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
    	
        createMenuBar();
        createMainPanel();
        createSlideNavigator();
        createBottomBar();
        layoutComponents();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu Arquivo
        JMenu fileMenu = new JMenu("Arquivo");
        fileMenu.add(new JMenuItem("Novo"));
        fileMenu.add(new JMenuItem("Abrir"));
        fileMenu.add(new JMenuItem("Salvar"));
        fileMenu.add(new JMenuItem("Salvar Como"));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem("Imprimir"));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem("Sair"));

        // Menu Inserir
        JMenu insertMenu = new JMenu("Inserir");
        insertMenu.add(new JMenuItem("Nova Slide"));
        insertMenu.add(new JMenuItem("Imagem"));
        insertMenu.add(new JMenuItem("Gráfico"));
        insertMenu.add(new JMenuItem("Tabela"));

        // Menu Design
        JMenu designMenu = new JMenu("Design");
        designMenu.add(new JMenuItem("Temas"));
        designMenu.add(new JMenuItem("Configurar Página"));

        // Menu Transições
        JMenu transitionsMenu = new JMenu("Transições");
        transitionsMenu.add(new JMenuItem("Transição de Slide"));

        // Menu Animações
        JMenu animationsMenu = new JMenu("Animações");
        animationsMenu.add(new JMenuItem("Animação de Objeto"));

        // Menu Apresentação de Slides
        JMenu slideShowMenu = new JMenu("Apresentação de Slides");
        slideShowMenu.add(new JMenuItem("Do Começo"));
        slideShowMenu.add(new JMenuItem("Do Slide Atual"));

        // Menu Revisão
        JMenu reviewMenu = new JMenu("Revisão");
        reviewMenu.add(new JMenuItem("Verificar Ortografia"));

        // Menu Exibição
        JMenu viewMenu = new JMenu("Exibição");
        viewMenu.add(new JMenuItem("Modo de Exibição Normal"));
        viewMenu.add(new JMenuItem("Classificador de Slides"));
        viewMenu.add(new JMenuItem("Modo de Leitura"));

        // Adicionando menus à barra de menu
        menuBar.add(fileMenu);
        menuBar.add(insertMenu);
        menuBar.add(designMenu);
        menuBar.add(transitionsMenu);
        menuBar.add(animationsMenu);
        menuBar.add(slideShowMenu);
        menuBar.add(reviewMenu);
        menuBar.add(viewMenu);

        setJMenuBar(menuBar);
    }


    private void createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
    }

    private void createSlideNavigator() {
        slideNavigator = new JPanel();
        slideNavigator.setLayout(new GridLayout(0, 1, 5, 5)); // Colunas dinâmicas, 1 linha, espaçamento de 5px
        slideNavigator.setPreferredSize(new Dimension(150, getHeight())); // Define a largura preferida do navegador de slides

        // Adiciona elementos quadrados ao navegador de slides
        for (int i = 0; i < 5; i++) {
            JPanel slide = new JPanel();
            slide.setPreferredSize(new Dimension(100, 100)); // Tamanho do slide
            slide.setBackground(Color.LIGHT_GRAY); // Cor de fundo
            slideNavigator.add(slide);
        }
        
    }
    
    private void createBottomBar() {
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Botão Modo de Apresentação
        JButton presentationModeButton = new JButton("Apresentação");
        presentationModeButton.addActionListener(e -> enterPresentationMode());
        bottomBar.add(presentationModeButton);

        // Contagem de Slides
        JLabel slideCountLabel = new JLabel("Slides: 0");
        bottomBar.add(slideCountLabel);

        // Botão Anotações
        JButton notesButton = new JButton("Anotações");
        bottomBar.add(notesButton);

        // Barra de Zoom
        JLabel zoomLabel = new JLabel("Zoom:");
        JSlider zoomSlider = new JSlider(0, 100, 50); // Valor inicial de 50
        bottomBar.add(zoomLabel);
        bottomBar.add(zoomSlider);

        // Adiciona a barra inferior ao JFrame
        add(bottomBar, BorderLayout.SOUTH);
    }
    
   

    private void layoutComponents() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, slideNavigator, mainPanel);
        splitPane.setDividerLocation(150);
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void loadImagesToSlideNavigator(String folderPath) {
        slideNavigator.removeAll(); // Remove todos os componentes existentes no slideNavigator

        File dir = new File(folderPath);
        FilenameFilter imageFilter = (dir1, name) -> name.toLowerCase().matches(".*\\.(jpg|png|gif)$");
        files = dir.listFiles(imageFilter);

        // Configura o BoxLayout para alinhar os componentes verticalmente
        slideNavigator.setLayout(new BoxLayout(slideNavigator, BoxLayout.Y_AXIS));
        slideNavigator.setPreferredSize(new Dimension(150, getHeight())); // Mantém a largura preferida do navegador de slides

        if (files != null && files.length > 0) {
            for (File file : files) {
                ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
                // Ajusta o tamanho da imagem para se encaixar na largura do slideNavigator
                Image image = imageIcon.getImage().getScaledInstance(175, 100, Image.SCALE_DEFAULT);
                JLabel label = new JLabel(new ImageIcon(image));
                label.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza as imagens
                // Adiciona uma margem de 10px em torno de cada imagem
                label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        displayImageInMainPanel(file.getAbsolutePath());
                    }
                });
                slideNavigator.add(label);
            }
        } else {
            JLabel noImagesLabel = new JLabel("Nenhuma imagem encontrada");
            noImagesLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza a mensagem
            // Adiciona uma margem de 10px em torno da mensagem
            noImagesLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            slideNavigator.add(noImagesLabel);
        }

        slideNavigator.revalidate();
        slideNavigator.repaint();
    }
    
    private void displayImageInMainPanel(String imagePath) {
        mainPanel.removeAll(); // Remove o conteúdo anterior do mainPanel

        // Carrega a imagem original
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image originalImage = originalIcon.getImage();
        int originalWidth = originalIcon.getIconWidth();
        int originalHeight = originalIcon.getIconHeight();

        // Calcula a nova largura e altura para manter a proporção
        double aspectRatio = (double) originalWidth / originalHeight;
        int newWidth = mainPanel.getWidth();
        int newHeight = (int) (mainPanel.getWidth() / aspectRatio);

        // Se a nova altura for maior que a altura do mainPanel, recalcula ambos
        if (newHeight > mainPanel.getHeight()) {
            newHeight = mainPanel.getHeight();
            newWidth = (int) (mainPanel.getHeight() * aspectRatio);
        }

        // Redimensiona a imagem
        Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        // Adiciona a imagem redimensionada ao mainPanel
        JLabel label = new JLabel(new ImageIcon(resizedImage));
        label.setHorizontalAlignment(JLabel.CENTER); // Centraliza a imagem no label
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(label, BorderLayout.CENTER);

        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private void enterPresentationMode() {
        if (files == null || files.length == 0) return; // Não entra no modo de apresentação se não houver imagens
        
        JFrame presentationFrame = new JFrame();
        presentationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        presentationFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        presentationFrame.setUndecorated(true);
        presentationFrame.setLayout(new BorderLayout());
        

        
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        presentationFrame.add(imageLabel, BorderLayout.CENTER);
        
        presentationFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Clique com o botão esquerdo
                    nextSlide(imageLabel, presentationFrame); // Chama o método nextSlide ao clicar com o botão esquerdo
                } else if (e.getButton() == MouseEvent.BUTTON3) { // Clique com o botão direito
                    previousSlide(imageLabel, presentationFrame); // Chama o método previousSlide ao clicar com o botão direito
                }
            }
        });
        
        // Exibe a primeira imagem
        displayImageInPresentationMode(0, imageLabel, presentationFrame);
        
        presentationFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    presentationFrame.dispose(); // Sai do modo de apresentação
                }
            }
        });
        
        presentationFrame.setVisible(true);
    }
    
    private void displayImageInPresentationMode(int index, JLabel imageLabel, JFrame presentationFrame) {
    	
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
    	
        if (index < 0 || index >= files.length) return; // Verifica se o índice é válido
        
        if (index >= files.length) { // Verifica se o índice está fora dos limites
            imageLabel.setText("Fim da apresentação");
            imageLabel.setFont(new Font("Arial", Font.BOLD, 30)); // Define a fonte para tornar o texto visível
            imageLabel.setForeground(Color.WHITE); // Define a cor do texto para branco
            presentationFrame.getContentPane().setBackground(Color.BLACK); // Define o fundo para preto
            imageLabel.setIcon(null); // Remove qualquer ícone que possa estar sendo exibido
        } else {
            // Seu código existente para exibir a imagem
            
            ImageIcon imageIcon = new ImageIcon(files[index].getAbsolutePath());
            Image image = imageIcon.getImage().getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(image));
        }
        
        
    }
    
    private void nextSlide(JLabel imageLabel, JFrame presentationFrame ) {
        if (files != null && currentSlideIndex < files.length - 1) {
            currentSlideIndex++;
            displayImageInPresentationMode(currentSlideIndex, imageLabel, presentationFrame);
        } else {
            // Opção para recomeçar a apresentação do início ou fechar
            currentSlideIndex = 0; // Recomeça do início
            displayImageInPresentationMode(currentSlideIndex, imageLabel, presentationFrame);
        }
    }
    
    private void previousSlide(JLabel imageLabel, JFrame presentationFrame) {
        if (files != null && currentSlideIndex > 0) {
            currentSlideIndex--;
            displayImageInPresentationMode(currentSlideIndex, imageLabel, presentationFrame);
        } else {
            // Opção para ir para o último slide se estiver no primeiro
            currentSlideIndex = files.length - 1; // Vai para o último slide
            displayImageInPresentationMode(currentSlideIndex, imageLabel, presentationFrame);
        }
    }

    public static void main(String[] args) {
    	
        SwingUtilities.invokeLater(() -> {
            PowerPointMockup ex = new PowerPointMockup();
            ex.loadImagesToSlideNavigator("C:\\Users\\gabri\\eclipse-workspace\\Powerpoint\\src\\images");
            ex.setVisible(true);
        });
        
    }
}