# run creation.sql first
USE bank_of_words_db;

insert into users (user_id, username, password_hash, email) VALUES (2, 'nika', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5', 'nsagi21@freeuni.edu.ge');

INSERT INTO tables (table_id, creator_id, table_name) VALUES (4, 2, 'country flags');
INSERT INTO tables (table_id, creator_id, table_name) VALUES (5, 2, 'animals');
INSERT INTO tables (table_id, creator_id, table_name) VALUES (6, 2, 'computer science terms');
INSERT INTO tables (table_id, creator_id, table_name) VALUES (7, 2, 'intellij shortcuts');

INSERT INTO words (word, definition, table_id) VALUES ('Penguin', 'A flightless seabird known for its distinctive black and white plumage, waddling walk, and excellent swimming abilities.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '1.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Koala', 'A cute, tree-dwelling marsupial native to Australia, recognized by its fluffy ears and penchant for munching on eucalyptus leaves.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '2.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Chameleon', 'A remarkable lizard capable of changing its skin color to match its environment, often used as a symbol of adaptability.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '3.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Red Panda', 'A small, adorable mammal native to the eastern Himalayas and southwestern China, with a striking reddish-brown coat and a fondness for bamboo.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '4.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Giraffe', 'A tall, long-necked herbivorous mammal native to Africa, characterized by its unique spotted coat and high-reaching browsing habits.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '5.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Arctic Fox', 'A beautiful, snow-white fox found in the Arctic regions, known for its thick fur and ability to survive in harsh, cold climates.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '6.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Hummingbird', 'A tiny, colorful bird capable of hovering in mid-air by rapidly flapping its wings, and often seen sipping nectar from flowers.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '7.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Sloth', 'A slow-moving, tree-dwelling mammal found in the rainforests of Central and South America, known for its relaxed lifestyle and leisurely pace.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '8.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Elephant', 'A majestic and intelligent mammal, the largest land animal on Earth, distinguished by its long trunk and impressive tusks.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '9.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Cheetah', 'A swift and graceful big cat native to Africa, famous for its incredible speed and agility when hunting prey.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '10.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Octopus', 'A highly intelligent marine creature with eight arms, a soft body, and the ability to change color and texture to blend into its surroundings.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '11.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Kangaroo', 'A marsupial from Australia known for its powerful hind legs, long tail, and ability to hop great distances.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '12.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Firefly', 'A fascinating insect capable of producing bioluminescent light, often seen glowing in the evening to attract mates.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '13.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Polar Bear', 'A massive carnivorous bear inhabiting the Arctic region, recognized by its white fur and strong swimming skills.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '14.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Peacock', 'A beautiful bird famous for its extravagant, iridescent tail feathers, which it displays in a stunning fan-like manner during courtship.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '15.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Orangutan', 'A highly intelligent and endangered great ape native to the rainforests of Southeast Asia, known for its distinctive reddish-brown fur.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '16.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Toucan', 'A colorful, large-billed bird found in tropical forests of Central and South America, often associated with its playful and vibrant appearance.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '17.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Hedgehog', 'A small, spiky mammal with a protective coat of sharp quills that roll into a ball when threatened.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '18.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Komodo Dragon', 'The world''s largest living lizard, native to Indonesia, possessing a venomous bite and exceptional hunting skills.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '19.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Panda', 'A beloved bear native to China, known for its striking black and white markings and bamboo-heavy diet.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '20.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Kookaburra', 'An Australian bird with a distinct laughing call, often referred to as the "laughing kookaburra."', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '21.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Platypus', 'An extraordinary egg-laying mammal from Australia, featuring a duck-like bill, webbed feet, and venomous spurs on its hind legs.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '22.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Quokka', 'A small marsupial native to Australia, known for its friendly and smiley appearance, making it a popular internet sensation.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '23.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Aye-Aye', 'A rare and unusual lemur from Madagascar, notable for its long, skeletal middle finger used for extracting insects from tree bark.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '24.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Narwhal', 'A medium-sized Arctic whale known for the long, spiral tusk protruding from the male''s upper jaw, which is actually an elongated tooth.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '25.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Okapi', 'A forest-dwelling mammal from Central Africa, resembling a mix of a giraffe and a horse, and known for its elusive nature.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '26.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Axolotl', 'A type of salamander found in Mexico, famous for its ability to regrow lost body parts and remain in a juvenile aquatic form throughout its life.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '27.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Mantis Shrimp', 'A small but incredibly colorful and aggressive marine crustacean, with remarkable eyesight and powerful raptorial appendages for hunting prey.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '28.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Quoll', 'A carnivorous marsupial native to Australia and New Guinea, featuring unique spots and a prehensile tail for climbing trees.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '29.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Fossa', 'A cat-like carnivorous mammal found in Madagascar, known for its slender body, keen hunting skills, and tree-climbing abilities.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '30.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Gharial', 'A large, fish-eating crocodile native to the Indian subcontinent, distinguished by its long, slender snout and sharp teeth.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '31.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Wombat', 'A sturdy, burrowing marsupial from Australia, recognized by its muscular frame, short legs, and powerful digging abilities.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '32.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Serval', 'A slender, medium-sized wild cat native to Africa, known for its impressive jumping and pouncing skills, which aid in catching prey.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '33.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Coati', 'A member of the raccoon family found in the Americas, distinguished by its long, ringed tail and a flexible snout used for foraging.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '34.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Blobfish', 'A gelatinous deep-sea fish with a unique appearance, often described as "ugly," though its odd structure helps it survive in high-pressure environments.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '35.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Fennec Fox', 'A small fox species native to North Africa, recognizable by its large ears, which help dissipate heat in hot desert environments.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '36.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Numbat', 'An insect-eating marsupial native to Western Australia, distinguished by its striking stripes and strong termite-hunting skills.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '37.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Markhor', 'A large, spiral-horned goat species found in mountainous regions of Central Asia, known for its impressive climbing abilities.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '38.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Tufted Deer', 'A small species of deer native to China, recognized by the prominent tuft of hair on its forehead and fang-like canines in males.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '39.jpg');
INSERT INTO words (word, definition, table_id) VALUES ('Pangolin', 'A unique, scaly mammal native to Africa and Asia, resembling an armored anteater and valued for its scales in traditional medicine.', 5);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), '40.jpg');

INSERT INTO words (word, definition, table_id) VALUES ('Algorithm', 'A step-by-step procedure used to solve a problem or achieve a particular goal', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Artificial Intelligence', 'The simulation of human intelligence processes by machines, especially computer systems', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Bit', 'The most basic unit of information in computing and digital communications', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Binary Search', 'A search algorithm that finds the position of a target value within a sorted array', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Compiler', 'A program that transforms source code written in a programming language into machine code', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Cloud Computing', 'The on-demand availability of computer system resources, especially data storage and computing power, without direct active management by the user', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Data Structure', 'A way of storing and organizing data in a computer so that it can be used efficiently', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Debugging', 'The process of finding and resolving defects or problems within a computer program', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Encryption', 'The process of converting data into a code to prevent unauthorized access', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Ethernet', 'A family of computer networking technologies commonly used in local area networks (LAN), metropolitan area networks (MAN) and wide area networks (WAN)', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Function', 'A sequence of program instructions that perform a specific task, packaged as a unit', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Firewall', 'A network security system that monitors and controls incoming and outgoing network traffic based on predetermined security rules', 6);
INSERT INTO words (word, definition, table_id) VALUES ('GUI', 'Graphical User Interface - allows users to interact with electronic devices through graphical icons', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Graphics Processing Unit', 'A specialized electronic circuit designed to rapidly manipulate and alter memory to accelerate the creation of images in a frame buffer intended for output to a display', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Hashing', 'A function used to map data of arbitrary size to data of a fixed size', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Hypertext Transfer Protocol', 'An application protocol for distributed, collaborative, and hypermedia information systems', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Inheritance', 'In OOP, a way to form new classes using classes that have already been defined', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Internet of Things', 'The interconnection of physical devices, vehicles, buildings, and other objects embedded with electronics, software, sensors, and network connectivity', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Java', 'A high-level, class-based, object-oriented programming language', 6);
INSERT INTO words (word, definition, table_id) VALUES ('JavaScript', 'A high-level, dynamic, untyped, and interpreted programming language', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Kernel', 'The core of a computer\'s operating system with control over everything in the system', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Kernel Panic', 'A safety measure taken by an operating system\'s kernel upon detecting an internal fatal error from which it cannot safely recover', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Linked List', 'A linear data structure where each element is a separate object', 6);
INSERT INTO words (word, definition, table_id) VALUES ('LAN', 'A computer network that interconnects computers within a limited area such as a residence, school, laboratory, university campus or office building', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Machine Learning', 'An application of AI that provides systems the ability to learn and improve from experience', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Malware', 'Any software intentionally designed to cause damage to a computer, server, client, or computer network', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Normalization', 'The process of organizing data in a database to avoid redundancy and improve data integrity', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Network Security', 'The process of taking physical and software preventative measures to protect the underlying networking infrastructure from unauthorized access, misuse, malfunction, modification, destruction, or improper disclosure', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Object-Oriented Programming', 'A programming paradigm based on the concept of "objects"', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Operating System', 'The software that manages computer hardware resources and provides common services for computer programs', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Polymorphism', 'In OOP, the provision of a single interface to entities of different types', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Python', 'A high-level, interpreted programming language that emphasizes code readability and simplicity', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Query', 'A request for data or information from a database', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Quantum Computing', 'The use of quantum-mechanical phenomena such as superposition and entanglement to perform computation', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Recursion', 'A method of solving problems where the solution depends on solutions to smaller instances of the same problem', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Router', 'A networking device that forwards data packets between computer networks', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Stack', 'A linear data structure which follows a particular order in which operations are performed', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Software Development Lifecycle', 'The process of designing, developing, testing, and deploying software', 6);
INSERT INTO words (word, definition, table_id) VALUES ('TCP/IP', 'A set of rules governing communications among all computers on the Internet', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Trojan Horse', 'A type of malware that is often disguised as legitimate software', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Unicode', 'A computing industry standard for the consistent encoding, representation, and handling of text', 6);
INSERT INTO words (word, definition, table_id) VALUES ('User Interface', 'The means by which the user and a computer system interact, in particular the use of input devices and software', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Virtual Reality', 'A simulated experience that can be similar to or completely different from the real world', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Virtual Machine', 'A software emulation of a computer system', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Web Services', 'A standardized way of integrating web-based applications using open standards over an internet protocol backbone', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Wi-Fi', 'A wireless networking technology that uses radio waves to provide wireless high-speed Internet and network connections', 6);
INSERT INTO words (word, definition, table_id) VALUES ('XML', 'A markup language that defines a set of rules for encoding documents in a format that is both human-readable and machine-readable', 6);
INSERT INTO words (word, definition, table_id) VALUES ('XML Schema', 'A description of a type of XML document that defines the structure of the document, as well as the data types and values that it may contain', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Yield', 'A keyword in many programming languages that produces a value in a generator function', 6);
INSERT INTO words (word, definition, table_id) VALUES ('YAML', 'A human-readable data serialization format that is often used for configuration files', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Zero-Day', 'A software vulnerability that has been discovered but does not yet have a patch', 6);
INSERT INTO words (word, definition, table_id) VALUES ('Zero-Knowledge Proof', 'A method by which one party can prove to another party that a given statement is true, without revealing any information beyond the validity of the statement itself', 6);


INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+N', 'To quickly open any class', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+Shift+N', 'To quickly open any file', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+Shift+F12', 'Toggle tools (maximize/minimize code window)', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+Space', 'Code completion', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Alt+F7', 'To find all places where a particular class, method, or variable is used in the whole project by positioning the caret at the symbol\'s name or at its usage in code', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+P', 'To quickly see the documentation for the class or method used at the editor\'s caret', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+Shift+Space', 'Basic code completion (the name of any class, method, or variable)', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+Shift+Enter', 'Complete statement', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+P', 'Parameter info (within method call arguments)', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+Q', 'Quick documentation lookup', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+Shift+V', 'Paste from recent buffers', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+D', 'Duplicate current line or selected block', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Double Shift', 'Search everywhere', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+F', 'Find', 7);
INSERT INTO words (word, definition, table_id) VALUES ('F3/Shift+F3', 'Find next / Find previous', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+R', 'Replace', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+Shift+F', 'Find in path', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+Shift+R', 'Replace in path', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+F9', 'Make project (compile modified and dependent)', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+Shift+F9', 'Compile selected file, package, or module', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Shift+F10', 'Run', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Shift+F9', 'Debug', 7);
INSERT INTO words (word, definition, table_id) VALUES ('F8', 'Step over', 7);
INSERT INTO words (word, definition, table_id) VALUES ('F7', 'Step into', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Shift+F7', 'Smart step into', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Shift+F8', 'Step out', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Alt+F9', 'Run to cursor', 7);
INSERT INTO words (word, definition, table_id) VALUES ('F9', 'Resume program', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+F8', 'Toggle breakpoint', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+Shift+F8', 'View breakpoints', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+Alt+J', 'Surround with Live Template', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+J', 'Insert Live Template', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+N', 'Go to class', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+Shift+N', 'Go to file', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+G', 'Go to line', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+B/Ctrl+Click', 'Go to declaration', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+Alt+B', 'Go to implementation(s)', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+Shift+B', 'Go to type declaration', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+S', 'Save all', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+Alt+S', 'Open Settings dialog', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Ctrl+Shift+A', 'Find Action', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Alt+Shift+Mouse Click', 'Add/remove a selection', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Alt+J', 'Select the next occurrence', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Shift+Alt+J', 'Unselect the next occurrence', 7);
INSERT INTO words (word, definition, table_id) VALUES ('Shift+Ctrl+Alt+J', 'Select all occurrences', 7);

INSERT INTO words (word, definition, table_id) VALUES ('Brazil', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Brazil.png');
INSERT INTO words (word, definition, table_id) VALUES ('Japan', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Japan.png');
INSERT INTO words (word, definition, table_id) VALUES ('Canada', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Canada.png');
INSERT INTO words (word, definition, table_id) VALUES ('South Korea', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'South-Korea.png');
INSERT INTO words (word, definition, table_id) VALUES ('India', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'India.png');
INSERT INTO words (word, definition, table_id) VALUES ('Switzerland', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Switzerland.png');
INSERT INTO words (word, definition, table_id) VALUES ('Kenya', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Kenya.png');
INSERT INTO words (word, definition, table_id) VALUES ('Australia', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Australia.png');
INSERT INTO words (word, definition, table_id) VALUES ('Georgia', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Georgia.png');
INSERT INTO words (word, definition, table_id) VALUES ('Germany', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Germany.png');
INSERT INTO words (word, definition, table_id) VALUES ('Norway', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Norway.png');
INSERT INTO words (word, definition, table_id) VALUES ('Argentina', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Argentina.png');
INSERT INTO words (word, definition, table_id) VALUES ('Jamaica', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Jamaica.png');
INSERT INTO words (word, definition, table_id) VALUES ('Sweden', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Sweden.png');
INSERT INTO words (word, definition, table_id) VALUES ('South Africa', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'South-Africa.png');
INSERT INTO words (word, definition, table_id) VALUES ('Egypt', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Egypt.png');
INSERT INTO words (word, definition, table_id) VALUES ('Indonesia', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Indonesia.png');
INSERT INTO words (word, definition, table_id) VALUES ('Mexico', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Mexico.png');
INSERT INTO words (word, definition, table_id) VALUES ('Italy', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Italy.png');
INSERT INTO words (word, definition, table_id) VALUES ('Spain', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Spain.png');
INSERT INTO words (word, definition, table_id) VALUES ('Bhutan', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Bhutan.png');
INSERT INTO words (word, definition, table_id) VALUES ('Kyrgyzstan', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Kyrgyzstan.png');
INSERT INTO words (word, definition, table_id) VALUES ('Lesotho', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Lesotho.png');
INSERT INTO words (word, definition, table_id) VALUES ('Bosnia and Herzegovina', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Bosnia-and-Herzegovina.png');
INSERT INTO words (word, definition, table_id) VALUES ('Seychelles', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Seychelles.png');
INSERT INTO words (word, definition, table_id) VALUES ('Mongolia', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Mongolia.png');
INSERT INTO words (word, definition, table_id) VALUES ('Palau', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Palau.png');
INSERT INTO words (word, definition, table_id) VALUES ('Eswatini', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Eswatini.png');
INSERT INTO words (word, definition, table_id) VALUES ('Maldives', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Maldives.png');
INSERT INTO words (word, definition, table_id) VALUES ('Suriname', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Suriname.png');
INSERT INTO words (word, definition, table_id) VALUES ('Comoros', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Comoros.png');
INSERT INTO words (word, definition, table_id) VALUES ('Brunei', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Brunei.png');
INSERT INTO words (word, definition, table_id) VALUES ('Nauru', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Nauru.png');
INSERT INTO words (word, definition, table_id) VALUES ('Kiribati', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Kiribati.png');
INSERT INTO words (word, definition, table_id) VALUES ('Tajikistan', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Tajikistan.png');
INSERT INTO words (word, definition, table_id) VALUES ('Mauritania', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Mauritania.png');
INSERT INTO words (word, definition, table_id) VALUES ('Mozambique', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Mozambique.png');
INSERT INTO words (word, definition, table_id) VALUES ('Paraguay', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Paraguay.png');
INSERT INTO words (word, definition, table_id) VALUES ('Qatar', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Qatar.png');
INSERT INTO words (word, definition, table_id) VALUES ('San Marino', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'San-Marino.png');
INSERT INTO words (word, definition, table_id) VALUES ('Togo', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Togo.png');
INSERT INTO words (word, definition, table_id) VALUES ('Tonga', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Tonga.png');
INSERT INTO words (word, definition, table_id) VALUES ('Turkmenistan', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Turkmenistan.png');
INSERT INTO words (word, definition, table_id) VALUES ('Uganda', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Uganda.png');
INSERT INTO words (word, definition, table_id) VALUES ('Uruguay', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Uruguay.png');
INSERT INTO words (word, definition, table_id) VALUES ('Uzbekistan', '', 4);
INSERT INTO word_images (word_id, image_name) VALUES (LAST_INSERT_ID(), 'Uzbekistan.png');