document.getElementById("formPegada").addEventListener("submit", async function (event) {
    event.preventDefault();

    const veiculo = document.getElementById("veiculo").value;
    const distancia = parseFloat(document.getElementById("distancia").value);
    const eficiencia = parseFloat(document.getElementById("eficiencia").value);
    const combustivel = document.getElementById("combustivel").value;

    const dados = {
        veiculo: veiculo,
        tipo_combustivel: combustivel,
        distancia_percorrida: distancia,
        eficiencia: eficiencia
    };

    try {
        const resposta = await fetch("http://localhost:8080/api/sendCarbon", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(dados)
        });

        if (!resposta.ok) {
            throw new Error("Erro na requisição: " + resposta.status);
        }

        const data = await resposta.json();

        document.getElementById("resultado").style.display = "block";
        document.getElementById("erro").style.display = "none";
        document.getElementById("resultado").textContent =
            "Pegada de Carbono: " + data.pegada_de_carbono.toFixed(2) + " kg CO₂e";
    } catch (error) {
        document.getElementById("erro").style.display = "block";
        document.getElementById("resultado").style.display = "none";
        document.getElementById("erro").textContent = "Erro: " + error.message;
    }
});