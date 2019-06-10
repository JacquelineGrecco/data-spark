# Youse: data-spark

Este projeto consiste em capturar dados fictícios, os quais são gerados por 2 micro serviços, com informações de orders e apólices. O objetivo do projeto é disponibilizar, através de tabelas relacionais armazenadas em um sqlite, os dados capturados.

## Relacionamento das Tabelas 

![diagrama ETLs](https://user-images.githubusercontent.com/22913973/59210246-8cc1d280-8b83-11e9-99fd-182cca07837b.png)

- As tabelas de apólices e orders se relacionam através do order_uuid, onde uma order pode criar diversas apólices. 
- A decisão em separar as apólices e orders pelos seus tipos foi tomada devido a probabilidade de criar novos KPIs com somente um tipo de apólice/order, evitando que precise carregar dados desnecessários para aquele KPI.

## KPIs 

- Base de Leads (Únicos ou não).
- Quantidade de Cotações (Únicas ou não).
- Preço de todas as cotações que tornaram-se apólices. 
- Quantidade de Cotações que não possuem uma precificação.
- Quantidade de Apólices (Únicas ou não).
- Vendas por canal, datas e afins.

_Para que seja possível obter esses e outros KPIs fictícios é necessário realizar um join entre as tabelas_

## Requisitos para Execução 

- sqlite
- scala 2.11.8
- spark 2.4.0
- maven como gerenciador de depedências do projeto

##Estrutura do Projeto 
```
| data-spark
    |src
       |main
         |resources 
         |scala 
            ...
            |database
            |insurance_policy
            |orders
            |utils
       |test
         |scala
           ...
            |database     
            |fixtures
```

- resources contém arquivos importantes para a config dos logs. 
- database  contém a classe de configuração do banco de dados. 
- orders    contém as classes e objetos com as informações de Orders.
- insurace_policy contém as classes e objetos com as informações de Apólices.
- utils contém objetos de configurações do spark. 
- fixtures contém os arquivos .json usados para as transformações. 
 
##Execução do Projeto

### Observações 

- Não se esqueça de adicionar os parâmetros necessários para a execução das classes OrdersBase e InsurancePolicyBase.
- Para os Intellij lover, é necessário a autenticação do seu usuário do Github no momento de clonar o projeto. 

###_Command-line lovers_

```git clone https://github.com/JacquelineGrecco/data-spark.git```
<br><br>
```mvn clean package``` 
<br><br>
```spark-submit --class br.com.youse.database.Database target/spark-youse-1.0.0``` 
<br><br>
 ```spark-submit --class br.com.youse.orders.OrdersBase target/spark-youse-1.0.0.jar```
 <br><br> 
 ```spark-submit --class br.com.youse.insurance_policy.InsurancePolicyBase target/spark-youse-1.0.0.jar```
 
 ###_Intellij lovers_ 
 
```File -> New -> Project From Version Control -> Git```
<br><br>
```URL: https://github.com/JacquelineGrecco/data-spark.git DIRECTORY: seu_path/data-spark``` 
<br><br> 
```src/main/br/com/youse/orders/OrdersBase -> Run```
<br>ou
```src/main/br/com/youse/insurance_policy/InsurancePolicyBase -> Run```
   
###_Banco de Dados_ 

O arquivo do banco de dados está localizado em [sqlite](database.db)

```sqlite3 ```
<br>
```.open database.db```
<br>
 ```.header on```
<br>
 ```select * from orders_base;```
 <br>ou  
 ```select * from policy_base;```