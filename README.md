# 24 Game Solver and Generator

This application supports solving and generating 24 game puzzles. The 24 game is a math puzzle game where
you add, subtract, multiply, and divide two numbers to find the number 24.

## Deploying

The application can be deployed in one of two ways:

1. Using the Helm chart to deploy within a Kubernetes cluster
2. Using the Ansible playbook to deploy on a virtual machine (or physical machine)

### Deploying with Helm

The Helm chart is not published but can be found in the `helm` directory. For configurable values,
refer to the `values.yaml` file

### Deploying with Ansible

To deploy with ansible, use the playbook and configuration in the `ansible` directory. You must configure
the `host_vars/game24.yml` file for your environment. Create a file like the following:

```yaml
ansible_user: ubuntu
ansible_ssh_private_key_file: ~/.ssh/id_rsa
ansible_host: 24game.mydomain.com
```
