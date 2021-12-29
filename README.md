# Vault Spring Boot

## Overview

This sample project demonstrates...

__What this is not?__

[TBD]

## Setting Up Vault

This example just uses the Vault sample dev server.

To start up the Vault Server:

```bash
vault server -dev
```

Input some secrets:

As an example...

```bash
vault kv put secret/app-secrets temp_pwd=ik1ahTho$a0eim8a api_key=a4db08b7-5729-4ba9-8c08-f2df493465a1 generic_secret=s3cr3t
```

Note:*
Above secrets are NOT real secrets. They are secret for purposefully for this demo repo.

## How it Works?

See `SecretsService.java`:

```java
```

All it does is it makes a REST API call to the Vault Server at the location of VAULT_ADDR and authentication with the VAULT_TOKEN.
It fetches back the secret at the predefined path as for the purpose of this demo:

```java
private static final String SECRET_URL_PATH = "/v1/secret/data/app-secrets";
```

We then lastly, filter out the required field of the secret we are after by navigating through the JSON tree via the external JSON Jackson Library.

_Note that for the simplicity of this particular demo I just fetch a string JSON response back rather than complicating things in mapping the returned JSON response into a POJO (which is probable but given that most of the data returned back are metadata which we are not really concerned about i just didn't feel it warrants that approach)._

Essentially what it is doing is making a curl request:

```bash
curl -H "X-VAULT-TOKEN: $VAULT_TOKEN" $VAULT_ADDR/v1/secret/data/app-secrets
```

And it returns the following HTTP response body:

as an example:

```json
{
  "request_id": "cd60061c-e16c-b7c4-3769-12b6e1a55e24",
  "lease_id": "",
  "renewable": false,
  "lease_duration": 0,
  "data": {
    "data": {
      "api_key": "a4db08b7-5729-4ba9-8c08-f2df493465a1",
      "generic_secret": "s3cr3t",
      "temp_pwd": "ik1ahTho"
    },
    "metadata": {
      "created_time": "2021-12-28T13:18:43.767322Z",
      "custom_metadata": null,
      "deletion_time": "",
      "destroyed": false,
      "version": 1
    }
  },
  "wrap_info": null,
  "warnings": null,
  "auth": null
}
```

So the Java code is merely a wrapper around the above.

### Vault Details in Application Configuration

Ensure the the `demo.mode` property is set to `APP-CONFIG`

```yaml
demo:
  mode: APP-CONFIG
```

__Configuration__

In the `application.yaml` configuration file we define the connection details to Vault:

e.g.
```yaml
vault:
  addr: http://127.0.0.1:8200
  token: your-token
```

__RestTemplate Configuration__

The base path of the "Vault Address" is wired into the configuration of the RestTemplate Spring Bean:

```java
@Configuration
public class RestTemplateConfig {

    @Value("${vault.addr}")
    private String vaultAddr;
    
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(vaultAddr));
        return restTemplate;
    }
}
```

In the `SecretsService.java` class:

```java
@Service
public class SecretsService {

    // other code omitted for brevity

    @Value("${vault.token}")
    private String vaultToken;

    // other code omitted for brevity
```

We autowire the `Vault Token` that is used for authentication to Vault.

### Using Environment Variables

Rather than hardcode the Vault details (especially the VAULT TOKEN which is a secret itself), we can inject these properties into the application via the following Environment Variables:

VAULT_ADDR
VAULT_TOKEN

Ensure the the `demo.mode` property is set to `ENV-VAR`

```yaml
demo:
  mode: ENV-VAR
```

Export the __VAULT_ADDR__ and __VAULT_TOKEN__ environment variables respectively.

As can be seen in the following code:

```java
  if (demoMode.equals(DemoMode.ENV_VAR.getDemoModeConf())){
    headers.set("X-VAULT-TOKEN", System.getenv("VAULT_TOKEN"));
  } else if (demoMode.equals(DemoMode.APP_CONFIG.getDemoModeConf())){
    headers.set("X-VAULT-TOKEN", vaultToken);
  }
```

We switch the method we obtain the VAULT_TOKEN accordingly.


## Authors

Colin But
