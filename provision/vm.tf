resource "aws_instance" "pg_vm" {
	ami = "ami-1ae2d279"
	instance_type = "t2.micro"
	key_name = "${var.ssh_key_name}"
	security_groups = [ "ido-allow-ssh", "ido-allow-http", ]
	tags {
		Name = "Payment Gateway"
	}

	provisioner "local-exec" {
		command = "echo ${self.public_ip} > hosts"
	}
	
	provisioner "remote-exec" {
		inline = [ "whoami", ]
		connection {
			type = "ssh"
			user = "ubuntu"
			private_key = "${file("${var.ssh_key_file}")}"
		}
	}
	
	provisioner "local-exec" {
		command = "ANSIBLE_HOST_KEY_CHECKING=False $HOME/.local/bin/ansible-playbook --private-key ${var.ssh_key_file} -u ubuntu -i '${aws_instance.pg_vm.public_ip},' vm.yml"
	}
}

output "instance" {
	value = "${aws_instance.pg_vm.id}"
}
output "ip" {
	value = "${aws_instance.pg_vm.public_ip}"
}
