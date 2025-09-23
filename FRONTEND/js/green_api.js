// --- lógica de calcular ---
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

        if (!resposta.ok) throw new Error("Erro na requisição: " + resposta.status);

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

// --- lógica de consultar ---
document.getElementById("formConsulta").addEventListener("submit", async function (event) {
    event.preventDefault();

    const codigo = document.getElementById("codigo").value;
    const btnDownload = document.getElementById("btnDownload");
    const btnLimpar = document.getElementById("btnLimpar");

    try {
        const resposta = await fetch(`http://localhost:8080/api/getVehicle/${codigo}`);
        if (!resposta.ok) throw new Error("Erro na consulta: " + resposta.status);

        const data = await resposta.json();

        // Mostrar apenas a pegada de carbono na tela
        document.getElementById("resultadoConsulta").style.display = "block";
        document.getElementById("erroConsulta").style.display = "none";
        document.getElementById("resultadoConsulta").textContent =
            `Pegada de Carbono: ${data.pegada_de_carbono.toFixed(2)} kg CO₂e`;

        // Ativar botão de download
        btnDownload.style.display = "inline-block";
        btnLimpar.style.display = "inline-block";

        btnDownload.onclick = () => {
            const conteudo = `
Veículo: ${data.veiculo}
Combustível: ${data.tipo_combustivel}
Distância percorrida: ${data.distancia_percorrida} km
Eficiência: ${data.eficiencia} km/l
Pegada de Carbono: ${data.pegada_de_carbono.toFixed(2)} kg CO₂e
UUID: ${data.uuid}
`;
            const blob = new Blob([conteudo], { type: "text/plain;charset=utf-8" });
            const link = document.createElement("a");
            link.href = URL.createObjectURL(blob);
            link.download = `${data.veiculo}_pegada_carbono.txt`;
            link.click();
            URL.revokeObjectURL(link.href);
        };

        btnLimpar.onclick = () => {
            document.getElementById("codigo").value = "";
            document.getElementById("resultadoConsulta").style.display = "none";
            document.getElementById("erroConsulta").style.display = "none";
            btnDownload.style.display = "none";
            btnLimpar.style.display = "none";
        };

    } catch (error) {
        document.getElementById("erroConsulta").style.display = "block";
        document.getElementById("resultadoConsulta").style.display = "none";
        document.getElementById("erroConsulta").textContent = "Erro: " + error.message;
        btnDownload.style.display = "none";
        btnLimpar.style.display = "none";
    }
});

// --- alternar telas ---
const btnCalcular = document.getElementById("btnCalcular");
const btnConsultar = document.getElementById("btnConsultar");
const telaCalcular = document.getElementById("telaCalcular");
const telaConsultar = document.getElementById("telaConsultar");

function ativarTela(tela) {
    telaCalcular.classList.remove("ativa");
    telaConsultar.classList.remove("ativa");
    btnCalcular.classList.remove("ativo");
    btnConsultar.classList.remove("ativo");

    if (tela === "calcular") {
        telaCalcular.classList.add("ativa");
        btnCalcular.classList.add("ativo");
    } else {
        telaConsultar.classList.add("ativa");
        btnConsultar.classList.add("ativo");
    }
}

btnCalcular.addEventListener("click", () => ativarTela("calcular"));
btnConsultar.addEventListener("click", () => ativarTela("consultar"));
