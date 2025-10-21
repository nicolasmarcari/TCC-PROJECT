// VARIÁVEIS GLOBAIS
let uuids = [];
let resultadosCSV = [];

// GERENCIAMENTO DE UUIDS
const inputUUID = document.getElementById('codigo');
const containerUUID = document.getElementById('uuidContainer');

inputUUID.addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        e.preventDefault();
        const uuid = this.value.trim();
        if (uuid && !uuids.includes(uuid)) {
            uuids.push(uuid);
            atualizarUuidTags();
        }
        this.value = '';
    }
});

function atualizarUuidTags() {
    containerUUID.querySelectorAll('.uuid-tag').forEach(tag => tag.remove());
    uuids.forEach((uuid, index) => {
        const tag = document.createElement('div');
        tag.className = 'uuid-tag';
        tag.innerHTML = `
            ${uuid} <button type="button" class="remove-btn" data-index="${index}">×</button>
        `;
        containerUUID.insertBefore(tag, inputUUID);
    });

    containerUUID.querySelectorAll('.remove-btn').forEach(btn => {
        btn.onclick = () => {
            const idx = parseInt(btn.getAttribute('data-index'));
            uuids.splice(idx, 1);
            atualizarUuidTags();
        };
    });
}

// CALCULAR PEGADA
document.getElementById("formPegada").addEventListener("submit", async function(event) {
    event.preventDefault();
    const veiculo = document.getElementById("veiculo").value;
    const distancia = parseFloat(document.getElementById("distancia").value);
    const eficiencia = parseFloat(document.getElementById("eficiencia").value);
    const combustivel = document.getElementById("combustivel").value;

    const dados = { veiculo, tipo_combustivel: combustivel, distancia_percorrida: distancia, eficiencia };

    try {
        const resposta = await fetch("/api/sendCarbon", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(dados)
        });

        if (!resposta.ok) throw new Error("Erro na requisição: " + resposta.status);
        const data = await resposta.json();

        document.getElementById("resultado").style.display = "block";
        document.getElementById("erro").style.display = "none";
        document.getElementById("resultado").textContent =
            `Pegada de Carbono: ${data.pegada_de_carbono.toFixed(2)} kg CO₂e \n\n UUID: ${data.uuid}`;

    } catch (error) {
        document.getElementById("erro").style.display = "block";
        document.getElementById("resultado").style.display = "none";
        document.getElementById("erro").textContent = "Erro: " + error.message;
    }
});

// CONSULTAR E GERAR CSV
document.getElementById("formConsulta").addEventListener('submit', async function(e) {
    e.preventDefault();
    if (uuids.length === 0) { alert("Adicione pelo menos um UUID."); return; }

    try {
        const promises = uuids.map(uuid =>
            fetch(`/api/getVehicle/${uuid}`)
                .then(r => { if(!r.ok) throw new Error(`UUID ${uuid}: ${r.status}`); return r.json(); })
        );

        const dados = await Promise.all(promises);
        resultadosCSV = dados.filter(d => d && d.uuid); // mantém apenas os objetos válidos

        if(resultadosCSV.length === 0) {
            alert("Nenhum dado válido retornado para os UUIDs.");
            return;
        }

        document.getElementById('btnDownload').style.display = "inline-block";
        document.getElementById('btnLimpar').style.display = "inline-block";
        alert("Consulta realizada! Clique em Download CSV para baixar.");

    } catch(err) {
        alert("Erro na consulta: " + err.message);
    }
});

// DOWNLOAD CSV
document.getElementById('btnDownload').addEventListener('click', () => {
    if(resultadosCSV.length === 0) { alert("Não há dados para baixar."); return; }

    const cabecalho = "Veículo,Combustível,Distância (km),Eficiência (km/l),Pegada de Carbono (kg CO₂e),UUID\n";
    const linhas = resultadosCSV.map(d =>
        `"${d.veiculo}","${d.tipo_combustivel}",${d.distancia_percorrida},${d.eficiencia},${d.pegada_de_carbono.toFixed(2)},"${d.uuid}"`
    ).join('\n');

    const blob = new Blob(['\uFEFF' + cabecalho + linhas], { type: 'text/csv;charset=utf-8' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = `relatorio_pegada_carbono_${new Date().toISOString().split('T')[0]}.csv`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
});

// LIMPAR UUIDs
document.getElementById('btnLimpar').addEventListener('click', () => {
    uuids = [];
    resultadosCSV = [];
    atualizarUuidTags();
    document.getElementById('btnDownload').style.display = "none";
    document.getElementById('btnLimpar').style.display = "none";
});

// ALTERNAR TELAS
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

btnCalcular.addEventListener('click', () => ativarTela('calcular'));
btnConsultar.addEventListener('click', () => ativarTela('consultar'));
