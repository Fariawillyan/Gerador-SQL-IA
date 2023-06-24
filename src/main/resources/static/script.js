var tabela;

function enviarDados() {
    var baseDados = document.getElementById("baseDados").value;
    var tabela = document.getElementById("tabela").value;
    var campos = document.getElementById("campos").value;
    var tipoQuery = document.getElementById("tipoQuery").value;
    var criterio = document.getElementById("criterio").value;


    var prompt = "### Postgres SQL " + baseDados + ", with their properties:\n#\n# " + tabela + "(id, name, department_id)\n# Salary_Payments(id, employee_id, amount, date)\n#\n###" + criterio + tipoQuery;

    var dados = {        
        prompt: prompt,
        temperature: 0,
        max_tokens: 150,
        top_p: 1.0,
        frequency_penalty: 0.0,
        presence_penalty: 0.0,
        stop: ["#", ";"]
        
    };

    $.ajax({
        url: "http://localhost:8080/api/generate-sql",
        type: "POST", data: JSON.stringify(dados),
        contentType: "application/json",
        success: function (response) {
            var resultadoQuery = document.querySelector('#resultadoQuery');   
              // Definir o valor do textarea
            resultadoQuery.value = tipoQuery + response;
        },

        error: function (xhr, status, error) {
            console.log("Ocorreu um erro ao enviar os dados:", error);
        }
    });

    

    /*document.addEventListener('DOMContentLoaded', function () {
        var myMultiselect = document.querySelector('#tabela');
        var selectedValues = [];     
        myMultiselect.addEventListener('change', function () { 
               selectedValues = Array.from(this.selectedOptions, option => option.value);
               console.log(selectedValues); });
        });
    */
       
}

