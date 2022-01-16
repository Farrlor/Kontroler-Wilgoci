# Prosty Kontroler Wilgoci
Aplikacja prezentująca zastosowanie reguł logiki rozmytej w języku JAVA w prostej symulacji dosyć prymitywnego kontrolera wilgoci. Algorytm rozmyty na bieżąco śledzi temperaturę oraz 
wilgotność, dzięki czemu zdolny jest do reakcji w przypadku nagłych zmian

Głównym problemem jest wykrycie czy wilgotność oraz temperatura zawierają się w powszechnych normach.
Aby agent odpowiednio zareagował na wykryte czynniki, system 
musi określić temperaturę otoczenia, stopień wilgotności oraz na 
podstawie pozyskanych danych dostosować odpowiedni poziom, 
suszenia/rozpraszania mgiełki.
Role czujników sprawdzających temperaturę oraz wilgotność 
spełniają pola tekstowe w które należy podać wartość. Wilgotność 
w czasie rzeczywistym będzie dążyć do wartości optymalnej dla 
podanej temperatury

Game Rules:

1. Maksymalny zakres odczytywanej temperatury zawiera się w 
przedziale od 20 do 27 stopni celsjusza. 
3. Maksymalny zakres odczytywanej wilgoci zawiera się w 
przedziale od: 0 do 100%.
4. Wilgotność spada/podnosi się 2% na 2 sekundy.
